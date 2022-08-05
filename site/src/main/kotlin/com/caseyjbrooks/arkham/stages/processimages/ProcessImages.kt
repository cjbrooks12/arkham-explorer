package com.caseyjbrooks.arkham.stages.processimages

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.Node
import com.caseyjbrooks.arkham.dag.path.InputPathNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.utils.destruct1
import com.caseyjbrooks.arkham.utils.rasterizeSvg
import com.caseyjbrooks.arkham.utils.withExtension
import com.caseyjbrooks.arkham.utils.withSuffix
import java.io.OutputStream
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.io.path.extension
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.readBytes

@Suppress("BlockingMethodInNonBlockingContext")
class ProcessImages : DependencyGraphBuilder {
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
            .getFilesInDirs("data")
            .filter { it.extension == "svg" }
            .forEach { inputPath ->
                addNodeAndEdge(
                    start = siteConfigNode,
                    newEndNode = StartPathNode(
                        meta = Node.Meta(
                            name = inputPath.invariantSeparatorsPathString,
                            tags = listOf("ProcessImages", "input", "svg"),
                        ),
                        inputPath = inputPath,
                    )
                )
            }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {
        graph
            .getNodesOfType<InputPathNode> { it.meta.tags == listOf("ProcessImages", "input", "svg") }
            .forEach { inputNodeQuery ->
                // copy the SVG over directly
                createOutputFile(
                    inputNode = inputNodeQuery,
                    tags = listOf("svg"),
                    getOutputPath = { it }
                ) { inputNodes, os ->
                    val (sourceSvg) = inputNodes.destruct1<InputPathNode>()
                    os.write(sourceSvg.realInputFile(graph).readBytes())
                }

                // rasterize the SVG to PNG with default size
                createOutputFile(
                    inputNode = inputNodeQuery,
                    tags = listOf("png", "default"),
                    getOutputPath = { it.withExtension("png") }
                ) { inputNodes, os ->
                    val (sourceSvg) = inputNodes.destruct1<InputPathNode>()
                    val inputImage = ImageIO.read(sourceSvg.realInputFile(graph).toFile())
                    ImageIO.write(inputImage, "PNG", os)
                }

                // rasterize the SVG to PNG with various scaled sizes
                listOf(24, 48, 64, 128, 256, 512, 1024).forEach { newHeight ->
                    createOutputFile(
                        inputNode = inputNodeQuery,
                        tags = listOf("png", "${newHeight}px"),
                        getOutputPath = { it.withSuffix("_${newHeight}px").withExtension("png") }
                    ) { inputNodes, os ->
                        val (sourceSvg) = inputNodes.destruct1<InputPathNode>()
                        rasterizeSvg(sourceSvg.realInputFile(graph), newHeight, os)
                    }
                }
            }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFile(
        inputNode: InputPathNode,
        getOutputPath: (Path) -> Path,
        tags: List<String>,
        render: (List<Node>, OutputStream) -> Unit,
    ) {
        val relativeOutputPath = Paths.get("data").relativize(inputNode.inputPath)
        val outputPath = getOutputPath(relativeOutputPath)
        addNodeAndEdge(
            start = inputNode,
            newEndNode = TerminalPathNode(
                meta = Node.Meta(
                    name = outputPath.invariantSeparatorsPathString,
                    tags = listOf("ProcessImages", "output") + tags,
                ),
                outputPath = outputPath,
                renderOutput = render,
            ),
        )
    }
}
