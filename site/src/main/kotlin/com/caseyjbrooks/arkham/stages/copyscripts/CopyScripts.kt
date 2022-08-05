package com.caseyjbrooks.arkham.stages.copyscripts

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.Node
import com.caseyjbrooks.arkham.dag.path.InputPathNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.utils.destruct1
import java.nio.file.Paths
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.name
import kotlin.io.path.readBytes

@Suppress("BlockingMethodInNonBlockingContext")
class CopyScripts : DependencyGraphBuilder {

    private var startIteration = Int.MIN_VALUE
    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        if (graph.containsNode { it is SiteConfigNode } && startIteration == Int.MIN_VALUE) {
            startIteration = graph.currentIteration
        }

        when (graph.currentIteration) {
            startIteration -> loadInputScripts()
            startIteration + 1 -> createOutputScripts()
        }
    }

    private suspend fun DependencyGraphBuilder.Scope.loadInputScripts() {
        val siteConfigNode = graph.getNodeOfType<SiteConfigNode>()

        graph
            .resourceService
            .getFilesInDir("app/build/distributions")
            .filterNot { it.name == "index.html" }
            .forEach { inputPath ->
                addNodeAndEdge(
                    start = siteConfigNode,
                    newEndNode = StartPathNode(
                        meta = Node.Meta(
                            name = inputPath.invariantSeparatorsPathString,
                            tags = listOf("CopyScripts", "input"),
                        ),
                        inputPath = inputPath,
                    )
                )
            }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputScripts() {
        graph
            .getNodesOfType<InputPathNode> {
                it.meta.tags == listOf("CopyScripts", "input")
            }
            .forEach { inputNodeQuery ->
                val relativeOutputPath = Paths.get("app/build/distributions").relativize(inputNodeQuery.inputPath)
                addNodeAndEdge(
                    start = inputNodeQuery,
                    newEndNode = TerminalPathNode(
                        meta = Node.Meta(
                            name = relativeOutputPath.invariantSeparatorsPathString,
                            tags = listOf("CopyScripts", "output"),
                        ),
                        outputPath = relativeOutputPath,
                        renderOutput = { inputNodes, os ->
                            val (sourceContentFile) = inputNodes.destruct1<InputPathNode>()
                            os.write(sourceContentFile.realInputFile(graph).readBytes())
                        },
                    ),
                )
            }
    }
}
