package com.caseyjbrooks.arkham.dag.renderer

import com.caseyjbrooks.arkham.dag.DependencyGraph
import com.caseyjbrooks.arkham.dag.Node
import com.caseyjbrooks.arkham.dag.path.OutputPathNode
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.response.respondOutputStream
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import java.nio.file.Paths
import kotlin.io.path.div
import kotlin.io.path.extension

/**
 * Start a local HTTP server and only render the output content when actually requested. This should not write any files
 * to the output directory.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LocalServerRenderer(val port: Int) : Renderer {
    override suspend fun start(graph: DependencyGraph): Unit = coroutineScope {
        graph
            .state
            .mapLatest {
                if (it == DependencyGraph.State.Ready) {
                    val entries = graph.nodes.filterIsInstance<OutputPathNode>()

                    println("Starting running on http://localhost:$port/")
                    embeddedServer(CIO, port = port) {
                        install(CallLogging)
                        routing {
                            get("/{fullPath...}") {
                                val path = call.parameters["fullPath"] ?: ""

                                val theseEntries = entries
                                theseEntries.also { println() }

                                val exactPath = Paths.get(path)
                                val pathAsIndexHtml = Paths.get(path) / "index.html"

                                val entryWithExactName = entries.singleOrNull { it.outputPath == exactPath }
                                val entryAsIndexHtml = entries.singleOrNull { it.outputPath == pathAsIndexHtml }

                                val entry = entryWithExactName ?: entryAsIndexHtml
                                val contentType = guessContentType(path, entry)

                                if (entry == null) {
                                    call.respondText(
                                        status = HttpStatusCode.NotFound,
                                        contentType = ContentType.Text.Html
                                    ) {
                                        "$path not found"
                                    }
                                } else {
                                    call.respondOutputStream(status = HttpStatusCode.OK, contentType = contentType) {
                                        entry.renderOutput(graph, this)
                                    }
                                }
                            }
                        }
                    }.start(wait = true)
                }
            }
            .launchIn(this)
    }

    override suspend fun renderDirtyOutputs(graph: DependencyGraph, dirtyOutputs: List<Node.Output>) {

    }

    private fun guessContentType(path: String, entry: OutputPathNode?): ContentType? {
        return guessContentTypeFromExtension(path.split(".").lastOrNull())
            ?: guessContentTypeFromExtension(entry?.outputPath?.extension)
    }

    private fun guessContentTypeFromExtension(extension: String?): ContentType? {
        return when {
            extension == "html" -> ContentType.Text.Html
            extension == "png" -> ContentType.Image.PNG
            extension == "svg" -> ContentType.Image.SVG
            extension == "json" -> ContentType.Application.Json
            extension == "js" -> ContentType.Text.JavaScript
            else -> null
        }
    }
}
