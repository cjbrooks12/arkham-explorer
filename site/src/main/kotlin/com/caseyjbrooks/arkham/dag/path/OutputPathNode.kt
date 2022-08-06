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

    val doRender: (List<Node>, OutputStream) -> Unit

    fun realOutputFile(graph: DependencyGraph): Path {
        return graph.config.outputDir / outputPath
    }

    override fun exists(graph: DependencyGraph): Boolean {
        return realOutputFile(graph).exists()
    }

    override suspend fun prepareOutput(graph: DependencyGraph) {
        realOutputFile(graph).apply {
            parent.createDirectories()
            if (!exists()) {
                createFile()
            }
        }
    }

    override suspend fun renderOutput(graph: DependencyGraph) {
        realOutputFile(graph).apply {
            this.outputStream().use {
                doRender(meta.edges.map { edge -> edge.start }, it)
            }
        }
    }

    override suspend fun renderOutput(graph: DependencyGraph, outputStream: OutputStream) {
        realOutputFile(graph).apply {
            outputStream.use {
                doRender(meta.edges.map { edge -> edge.start }, it)
            }
        }
    }
}
