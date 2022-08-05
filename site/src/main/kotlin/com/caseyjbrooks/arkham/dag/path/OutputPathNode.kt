package com.caseyjbrooks.arkham.dag.path

import com.caseyjbrooks.arkham.dag.DependencyGraph
import com.caseyjbrooks.arkham.dag.Node
import com.caseyjbrooks.arkham.utils.SiteConfiguration
import java.io.OutputStream
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.outputStream

interface OutputPathNode : Node.Output {
    /**
     * This path is relative to [SiteConfiguration.outputDir]
     */
    val outputPath: Path

    val renderOutput: (List<Node>, OutputStream) -> Unit

    fun realOutputFile(graph: DependencyGraph): Path {
        return graph.config.outputDir / outputPath
    }

    override fun exists(graph: DependencyGraph): Boolean {
        return realOutputFile(graph).exists()
    }

    override fun prepareOutput(graph: DependencyGraph) {
        realOutputFile(graph).apply {
            parent.createDirectories()
            if (!exists()) {
                createFile()
            }
        }
    }

    override fun renderOutput(graph: DependencyGraph) {
//        println("rendering output file: ${this.meta.name}")
        realOutputFile(graph).apply {
            this.outputStream().use {
                renderOutput(meta.edges.map { edge -> edge.start }, it)
            }
        }
    }
}
