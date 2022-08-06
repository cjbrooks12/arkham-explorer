package com.caseyjbrooks.arkham.stages.assets

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
import javax.imageio.ImageIO
import kotlin.io.path.div
import kotlin.io.path.extension
import kotlin.io.path.readBytes

@Suppress("BlockingMethodInNonBlockingContext")
class RasterizeSvgs : DependencyGraphBuilder {
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
            .filter { it.extension == "svg" }
            .forEach { inputPath ->
                addNodeAndEdge(
                    start = siteConfigNode,
                    newEndNode = StartPathNode(
                        tags = listOf("RasterizeSvgs", "input", "svg"),
                        baseInputDir = graph.config.assetsDir,
                        inputPath = inputPath,
                    )
                )
            }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {
        graph
            .getNodesOfType<InputPathNode> { it.meta.tags == listOf("RasterizeSvgs", "input", "svg") }
            .forEach { inputNodeQuery ->
                // copy the SVG over directly
                createOutputFile(
                    inputNode = inputNodeQuery,
                    tags = listOf("svg"),
                    getOutputPath = { it }
                ) { inputNodes, os ->
                    val (sourceSvg) = inputNodes.destruct1<InputPathNode>()
                    os.write(sourceSvg.realInputFile().readBytes())
                }

                // rasterize the SVG to PNG with default size
                createOutputFile(
                    inputNode = inputNodeQuery,
                    tags = listOf("png", "default"),
                    getOutputPath = { it.withExtension("png") }
                ) { inputNodes, os ->
                    val (sourceSvg) = inputNodes.destruct1<InputPathNode>()
                    val inputImage = ImageIO.read(sourceSvg.realInputFile().toFile())
                    ImageIO.write(inputImage, "PNG", os)
                }

                // rasterize the SVG to PNG with various scaled sizes
                listOf(24, 48, 64, 128, 256, 512, 1024).forEach { newHeight ->
                    createOutputFile(
                        inputNode = inputNodeQuery,
                        tags = listOf("png", "${newHeight}px"),
                        getOutputPath = { it.withSuffix("-${newHeight}px").withExtension("png") }
                    ) { inputNodes, os ->
                        val (sourceSvg) = inputNodes.destruct1<InputPathNode>()
                        rasterizeSvg(sourceSvg.realInputFile(), newHeight, os)
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
        val outputPath = getOutputPath(inputNode.inputPath)
        addNodeAndEdge(
            start = inputNode,
            newEndNode = TerminalPathNode(
                tags = listOf("RasterizeSvgs", "output") + tags,
                baseOutputDir = graph.config.outputDir / "assets",
                outputPath = outputPath,
                doRender = render,
            ),
        )
    }
}
