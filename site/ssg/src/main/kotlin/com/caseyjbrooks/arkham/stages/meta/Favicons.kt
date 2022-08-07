package com.caseyjbrooks.arkham.stages.meta

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.path.InputPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.utils.destruct1
import com.caseyjbrooks.arkham.utils.rasterizeSvg
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.nio.file.Paths
import javax.imageio.ImageIO


class Favicons : DependencyGraphBuilder {

    private var startIteration = Int.MIN_VALUE
    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        if (graph.containsNode { it is SiteConfigNode } && startIteration == Int.MIN_VALUE) {
            startIteration = graph.currentIteration
        }

        when (graph.currentIteration) {
            startIteration + 1 -> {
                val nightOfTheZealotSvgInputNode = graph
                    .getNodeOfType<InputPathNode> {
                        it.meta.tags == listOf("RasterizeSvgs", "input", "svg") &&
                            it.inputPath == Paths.get("night-of-the-zealot.svg")
                    }

                val formats = ImageIO.getWriterFormatNames()
                formats.also { println() }

                addNodeAndEdge(
                    start = nightOfTheZealotSvgInputNode,
                    newEndNode = TerminalPathNode(
                        baseOutputDir = graph.config.outputDir,
                        outputPath = Paths.get("favicon.ico"),
                        doRender = { nodes, os ->
                            val (svg) = nodes.destruct1<InputPathNode>()

                            // TwelveMonkeys doesnt seem to like converting directly from SVG to ICO, so we rasterize
                            // it in-memory first to a ByteArrayOutputStream
                            val bos = ByteArrayOutputStream().also { bos ->
                                bos.use {
                                    rasterizeSvg(svg.realInputFile(), 16, bos, 16)
                                }
                            }

                            // we then copy that ByteArrayOutputStream to the original OutputStream
                            val image: BufferedImage = ImageIO.read(bos.toByteArray().inputStream())
                            ImageIO.write(image, "ICO", os)
                        }
                    )
                )
            }
        }
    }
}
