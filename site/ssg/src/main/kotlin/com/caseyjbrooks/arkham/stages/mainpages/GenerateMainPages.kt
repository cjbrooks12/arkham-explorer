package com.caseyjbrooks.arkham.stages.mainpages

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.path.InputPathNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.site.BuildConfig
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.utils.destruct1
import com.caseyjbrooks.arkham.utils.withExtension
import kotlin.io.path.extension
import kotlin.io.path.readText

@Suppress("BlockingMethodInNonBlockingContext")
class GenerateMainPages : DependencyGraphBuilder {
    private val replacements = listOf(
        "baseUrl" to BuildConfig.BASE_URL,
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
            .getFilesInDirs(graph.config.pagesDir)
            .forEach { inputPath ->
                addNodeAndEdge(
                    start = siteConfigNode,
                    newEndNode = StartPathNode(
                        baseInputDir = graph.config.pagesDir,
                        inputPath = inputPath,
                        tags = listOf("GenerateMainPages", "input"),
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
                val outputPath = inputNodeQuery.inputPath.withExtension("html")
                addNodeAndEdge(
                    start = inputNodeQuery,
                    newEndNode = TerminalPathNode(
                        baseOutputDir = graph.config.outputDir,
                        outputPath = outputPath,
                        tags = listOf("ProcessImages", "output"),
                        doRender = { inputNodes, os ->
                            val (sourceContentFile) = inputNodes.destruct1<InputPathNode>()
                            val sourceText = sourceContentFile.realInputFile().readText()
                            val outputText = processByExtension(sourceText, sourceContentFile.inputPath.extension)
                            os.write(outputText.toByteArray())
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

