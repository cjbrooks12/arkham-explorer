package com.caseyjbrooks.arkham

import com.caseyjbrooks.arkham.site.BuildConfig
import com.caseyjbrooks.arkham.stages.copyscripts.CopyScripts
import com.caseyjbrooks.arkham.stages.mainpages.GenerateMainPages
import com.caseyjbrooks.arkham.stages.processimages.ProcessImages
import com.caseyjbrooks.arkham.utils.cache.CacheService
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respondOutputStream
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.io.path.Path
import kotlin.io.path.div

fun main(): Unit = runBlocking {
    withContext(Dispatchers.IO) {
        val repoRoot = Path("./../")
        val cacheService = CacheService(
            rootDir = repoRoot,
            outputDir = repoRoot / "build/dist",
            hashesDir = repoRoot / "build/cache/site",
            httpCacheDir = repoRoot / "build/cache/http",
        )

        val stages = listOf(
            ProcessImages(cacheService),
//            FetchExpansionData(cacheService),
            CopyScripts(cacheService),
            GenerateMainPages(cacheService),
        )

        val allInputs = stages
            .map { async { it.process() } }
            .awaitAll()
            .flatten()

        cacheService.createIndex(allInputs)
        cacheService.verifyIndex()

        if (BuildConfig.DEBUG) {
            println("Starting server on http://localhost:8080/")
            embeddedServer(CIO, port = 8080) {
                routing {
                    get("/{fullPath...}") {
                        val path = call.parameters["fullPath"]!!
                        println("[GET] $path")
                        val contentType = when {
                            path.endsWith(".html") -> ContentType.Text.Html
                            path.endsWith(".png") -> ContentType.Image.PNG
                            path.endsWith(".svg") -> ContentType.Image.SVG
                            path.endsWith(".json") -> ContentType.Application.Json
                            path.endsWith(".js") -> ContentType.Text.JavaScript
                            else -> null
                        }
                        call.respondOutputStream(status = HttpStatusCode.OK, contentType = contentType) {
                            cacheService.renderFilePathExternally(path, this)
                        }
                    }
                }
            }.start(wait = true)
        } else {
            cacheService.prepareOutputDirectory()
            cacheService.renderAllOutputs()
        }
    }
}
