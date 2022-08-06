package com.caseyjbrooks.arkham.stages.assets

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.path.InputPathNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.utils.destruct1
import kotlin.io.path.div
import kotlin.io.path.extension
import kotlin.io.path.readBytes

@Suppress("BlockingMethodInNonBlockingContext")
class CopyOtherAssets : DependencyGraphBuilder {
    private var startIteration = Int.MIN_VALUE
    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        if (graph.containsNode { it is SiteConfigNode } && startIteration == Int.MIN_VALUE) {
            startIteration = graph.currentIteration
        }

        when (graph.currentIteration) {
            startIteration -> loadInputImages()
            startIteration + 1 -> createOutputFiles()
        }
    }

    private suspend fun DependencyGraphBuilder.Scope.loadInputImages() {
        val siteConfigNode = graph.getNodeOfType<SiteConfigNode>()
        graph
            .resourceService
            .getFilesInDirs(graph.config.assetsDir)
            .filter { it.extension != "svg" }
            .forEach { inputPath ->
                addNodeAndEdge(
                    start = siteConfigNode,
                    newEndNode = StartPathNode(
                        tags = listOf("CopyOtherAssets", "input"),
                        baseInputDir = graph.config.assetsDir,
                        inputPath = inputPath,
                    )
                )
            }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {
        graph
            .getNodesOfType<InputPathNode> { it.meta.tags == listOf("CopyOtherAssets", "input") }
            .forEach { inputNode ->
                // copy the file over directly
                val outputPath = inputNode.inputPath
                addNodeAndEdge(
                    start = inputNode,
                    newEndNode = TerminalPathNode(
                        tags = listOf("CopyOtherAssets", "output"),
                        baseOutputDir = graph.config.outputDir / "assets",
                        outputPath = outputPath,
                        doRender = { inputNodes, os ->
                            val (sourceFile) = inputNodes.destruct1<InputPathNode>()
                            os.write(sourceFile.realInputFile().readBytes())
                        },
                    ),
                )
            }
    }

}
