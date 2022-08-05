package com.caseyjbrooks.arkham.stages.mainpages

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.Node
import com.caseyjbrooks.arkham.dag.path.InputPathNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.site.BuildConfig
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.utils.destruct1
import com.caseyjbrooks.arkham.utils.withExtension
import java.nio.file.Paths
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.readBytes

@Suppress("BlockingMethodInNonBlockingContext")
class GenerateMainPages : DependencyGraphBuilder {
    private val replacements = listOf(
        "siteBaseUrl" to BuildConfig.SITE_BASE_URL,
        "apiBaseUrl" to BuildConfig.API_BASE_URL,
    )

    private var startIteration = Int.MIN_VALUE
    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        if (graph.containsNode { it is SiteConfigNode } && startIteration == Int.MIN_VALUE) {
            startIteration = graph.currentIteration
        }

        when (graph.currentIteration) {
            startIteration -> loadInputContentFiles()
            startIteration + 1 -> createOutputFiles()
        }
    }

    private suspend fun DependencyGraphBuilder.Scope.loadInputContentFiles() {
        val siteConfigNode = graph.getNodeOfType<SiteConfigNode>()

        graph
            .resourceService
            .getFilesInDirs("content")
            .forEach { inputPath ->
                addNodeAndEdge(
                    start = siteConfigNode,
                    newEndNode = StartPathNode(
                        meta = Node.Meta(
                            name = inputPath.invariantSeparatorsPathString,
                            tags = listOf("GenerateMainPages", "input"),
                        ),
                        inputPath = inputPath,
                    )
                )
            }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {
        graph
            .getNodesOfType<InputPathNode> {
                it.meta.tags == listOf("GenerateMainPages", "input")
            }
            .forEach { inputNodeQuery ->
                val relativeOutputPath = Paths.get("content").relativize(inputNodeQuery.inputPath)
                val outputPath = relativeOutputPath.withExtension("html")
                addNodeAndEdge(
                    start = inputNodeQuery,
                    newEndNode = TerminalPathNode(
                        meta = Node.Meta(
                            name = outputPath.invariantSeparatorsPathString,
                            tags = listOf("ProcessImages", "output"),
                        ),
                        outputPath = outputPath,
                        renderOutput = { inputNodes, os ->
                            val (sourceContentFile) = inputNodes.destruct1<InputPathNode>()
                            os.write(sourceContentFile.realInputFile(graph).readBytes())
                        },
                    ),
                )
            }
    }

    private fun processByExtension(originalText: String, extension: String): String {
        val proprocessedText = preprocessContent(originalText)
        return when (extension) {
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


//
//    override suspend fun process(): Iterable<Cacheable.Input<*, *>> {
//        return resourceService
//            .getFilesInDirs("content")
//            .map { inputPath ->
//                val relativeInputPath = Paths.get("content").relativize(inputPath)
//
//                InputPath(
//                    processor = "GenerateMainPages",
//                    version = VERSION,
//                    inputPath = inputPath,
//                    rootDir = cacheService.rootDir,
//                    hashesDir = cacheService.hashesDir,
//                    outputs = {
//                        listOf(
//                            OutputFromPath(
//                                outputDir = cacheService.outputDir,
//                                outputPath = relativeInputPath.withExtension("html"),
//                                render = { input, os ->
//                                    os.use {
//                                        val originalText = input.realInput.readText()
//                                        val processedText = processByExtension(originalText, input.realInput.extension)
//                                        it.write(processedText.toByteArray())
//                                    }
//                                }
//                            )
//                        )
//                    }
//                )
//            }
//    }
//

//}
//
