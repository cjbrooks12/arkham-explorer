package com.caseyjbrooks.arkham.dag.http

import com.caseyjbrooks.arkham.dag.DependencyGraph
import com.caseyjbrooks.arkham.dag.Node
import com.caseyjbrooks.arkham.utils.md5
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.writeText
import kotlin.time.Duration.Companion.seconds

private val MAX_AGE_REGEX = """max-age=(\d+), public""".toRegex()
val prettyJson = Json {
    prettyPrint = true
    isLenient = true
}

@OptIn(ExperimentalSerializationApi::class)
interface InputHttpNode : Node.Input {

    val httpClient: HttpClient
    val url: String

    private fun cachedResponseFile(graph: DependencyGraph): Path {
        return graph.config.httpCacheDir / "${url.replace("\\W+".toRegex(), "-")}/${url.md5}/response.json"
    }

    private fun cachedMetadataFile(graph: DependencyGraph): Path {
        return graph.config.httpCacheDir / "${url.replace("\\W+".toRegex(), "-")}/${url.md5}/metadata.json"
    }

    override suspend fun dirty(graph: DependencyGraph): Boolean {
        val responseFile = cachedResponseFile(graph)
        val metadataFile = cachedMetadataFile(graph)

        if (!responseFile.exists()) return true
        if (!metadataFile.exists()) return true

        val metadata = metadataFile.inputStream().use {
            prettyJson.decodeFromStream(HttpMetadata.serializer(), it)
        }

        return metadata.isExpired() || !metadata.clean
    }

    private suspend fun fetchAndCache(graph: DependencyGraph, markAsClean: Boolean) {
        val responseFile = cachedResponseFile(graph)
        val metadataFile = cachedMetadataFile(graph)

        val shouldRefresh = if(responseFile.exists() && metadataFile.exists()) {
            // check if we are past the max-age of the response
            val metadata = metadataFile.inputStream().use {
                prettyJson.decodeFromStream(HttpMetadata.serializer(), it)
            }

            metadata.isExpired()
        } else {
            // a file was missing, we need to refresh
            true
        }

        if(shouldRefresh) {
            val response = httpClient.get(url)
            val bodyJsonString = response.bodyAsText()
            val headers = response.headers

            cachedResponseFile(graph).apply {
                parent.createDirectories()
                if (!exists()) createFile()

                val bodyAsJsonElement = prettyJson.decodeFromString(JsonElement.serializer(), bodyJsonString)
                val bodyAsPrettyJsonString = prettyJson.encodeToString(JsonElement.serializer(), bodyAsJsonElement)

                writeText(bodyAsPrettyJsonString)
            }

            cachedMetadataFile(graph).apply {
                parent.createDirectories()
                if (!exists()) createFile()

                val maxAge = headers[HttpHeaders.CacheControl]?.let {
                    MAX_AGE_REGEX.matchEntire(it)?.groupValues?.get(1)?.toIntOrNull()
                } ?: -1
                val metadata = HttpMetadata(
                    maxAge = maxAge.seconds,
                    lastFetched = Clock.System.now(),
                    clean = markAsClean,
                )
                val metadataJson = prettyJson.encodeToString(HttpMetadata.serializer(), metadata)

                writeText(metadataJson)
            }
        } else if(markAsClean) {
            // rewrite the same metadata, but mark it as clean
            val metadata = metadataFile.inputStream().use {
                prettyJson.decodeFromStream(HttpMetadata.serializer(), it)
            }
            val metadataJson = prettyJson.encodeToString(HttpMetadata.serializer(), metadata.copy(clean = true))

            metadataFile.writeText(metadataJson)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun <T> getResponse(graph: DependencyGraph, serializer: KSerializer<T>): T {
        fetchAndCache(graph, false)
        return cachedResponseFile(graph).let { cachedResponse ->
            cachedResponse.inputStream().let { stream ->
                prettyJson.decodeFromStream(serializer, stream)
            }
        }
    }

    override suspend fun markClean(graph: DependencyGraph) {
        fetchAndCache(graph, true)
    }

}
