package com.caseyjbrooks.arkham.utils.server

import io.ktor.http.ContentType

class LocalServer {

//    fun CoroutineScope.start(index: Index, port: Int) {
//        println("Starting server on http://localhost:$port/")
//        embeddedServer(CIO, port = port) {
//            install(CallLogging)
//            routing {
//                get("/{fullPath...}") {
//                    val path = call.parameters["fullPath"] ?: ""
//
//                    val entry = index.getEntry(path)
//                    val contentType = guessContentType(path, entry)
//
//                    if(entry == null) {
//                        call.respondText(status = HttpStatusCode.NotFound, contentType = ContentType.Text.Html) {
//                            "$path not found"
//                        }
//                    } else {
//                        call.respondOutputStream(status = HttpStatusCode.OK, contentType = contentType) {
//                            entry.renderToStream(this)
//                        }
//                    }
//                }
//            }
//        }.start(wait = true)
//    }

//    private fun guessContentType(path: String, entry: IndexEntry<*, *>?): ContentType? {
//        return guessContentTypeFromExtension(path.split(".").lastOrNull())
//            ?: guessContentTypeFromExtension(entry?.output?.realOutput?.extension)
//    }

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
