package com.caseyjbrooks.arkham.stages.content

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.prettyJson
import com.caseyjbrooks.arkham.dag.path.InputPathNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.utils.destruct1
import com.caseyjbrooks.arkham.utils.getOutputExtension
import com.caseyjbrooks.arkham.utils.processByExtension
import com.caseyjbrooks.arkham.utils.withExtension
import com.caseyjbrooks.arkham.utils.withPrefix
import com.copperleaf.arkham.models.ArkhamExplorerStaticPage
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readText

class StaticPages : DependencyGraphBuilder {
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
                        tags = listOf("StaticPages", "input"),
                    )
                )
            }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {
        graph
            .getNodesOfType<InputPathNode> {
                it.meta.tags == listOf("StaticPages", "input")
            }
            .forEach { inputNodeQuery ->
                val outputPath = inputNodeQuery.inputPath.withExtension(
                    getOutputExtension(inputNodeQuery.inputPath.extension)
                )
                addNodeAndEdge(
                    start = inputNodeQuery,
                    newEndNode = TerminalPathNode(
                        baseOutputDir = graph.config.outputDir,
                        outputPath = outputPath.withPrefix("pages").withExtension("json"),
                        tags = listOf("StaticPages", "output"),
                        doRender = { inputNodes, os ->
                            val (sourceContentFile) = inputNodes.destruct1<InputPathNode>()
                            val sourceText = sourceContentFile.realInputFile().readText()
                            val outputText = processByExtension(sourceText, sourceContentFile.inputPath.extension)

                            val staticPageJson = ArkhamExplorerStaticPage(
                                title = sourceContentFile.inputPath.nameWithoutExtension,
                                slug = sourceContentFile.inputPath.nameWithoutExtension,
                                content = outputText,
                            )

                            prettyJson.encodeToStream(ArkhamExplorerStaticPage.serializer(), staticPageJson, os)
                        },
                    ),
                )
            }
    }
}
