package com.caseyjbrooks.arkham.stages.mainpages

import com.caseyjbrooks.arkham.site.BuildConfig
import com.caseyjbrooks.arkham.stages.ProcessingStage
import com.caseyjbrooks.arkham.utils.asIndexHtml
import com.caseyjbrooks.arkham.utils.cache.CacheService
import com.caseyjbrooks.arkham.utils.cache.Cacheable
import java.nio.file.Paths
import kotlin.io.path.extension
import kotlin.io.path.readText

class GenerateMainPages(
    private val cacheService: CacheService
) : ProcessingStage {
    companion object {
        private const val VERSION = 1L
    }

    private val replacements = listOf(
        "siteBaseUrl" to BuildConfig.SITE_BASE_URL,
        "apiBaseUrl" to BuildConfig.API_BASE_URL,
    )

    override suspend fun process(): Iterable<Cacheable.Input<*, *>> {
        return cacheService
            .getFilesInDirs("content")
            .map { inputPath ->
                val relativeInputPath = Paths.get("content").relativize(inputPath)

                Cacheable.Input.CachedPath(
                    processor = "GenerateMainPages",
                    version = GenerateMainPages.VERSION,
                    inputPath = inputPath,
                    rootDir = cacheService.rootDir,
                    outputs = {
                        listOf(
                            Cacheable.Output.CachedPath(
                                outputDir = cacheService.outputDir,
                                outputPath = relativeInputPath.asIndexHtml(),
                                render = { input, os ->
                                    os.use {
                                        val originalText = input.realInput.readText()
                                        val processedText = processByExtension(originalText, input.realInput.extension)
                                        it.write(processedText.toByteArray())
                                    }
                                }
                            )
                        )
                    }
                )
            }
    }

    private fun processByExtension(originalText: String, extension: String): String {
        val proprocessedText = preprocessContent(originalText)
        return when(extension) {
            "html" -> processHtml(proprocessedText)
            "md" -> processMarkdown(proprocessedText)
            else -> error("Unknown file extension in site content")
        }
    }

    private fun preprocessContent(inputText: String): String {
        return replacements
            .fold(inputText) { acc, (key, value) ->
                acc.replace("{{$key}}", value)
            }
    }

    private fun processHtml(inputText: String): String {
        return inputText
    }

    private fun processMarkdown(inputText: String): String {
        return inputText
    }
}

