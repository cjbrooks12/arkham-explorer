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
    val baseOutputDir: Path

    /**
     * This path is relative to [SiteConfiguration.outputDir]
     */
    val outputPath: Path

    val doRender: suspend (List<Node>, OutputStream) -> Unit

    fun realOutputFile(): Path {
        return baseOutputDir / outputPath
    }

    override fun exists(graph: DependencyGraph): Boolean {
        return realOutputFile().exists()
    }

    override suspend fun prepareOutput(graph: DependencyGraph) {
        realOutputFile().apply {
            parent.createDirectories()
            if (!exists()) {
                createFile()
            }
        }
    }

    override suspend fun renderOutput(graph: DependencyGraph) {
        realOutputFile().apply {
            this.outputStream().use {
                doRender(meta.edges.map { edge -> edge.start }, it)
            }
        }
    }

    override suspend fun renderOutput(graph: DependencyGraph, outputStream: OutputStream) {
        realOutputFile().apply {
            outputStream.use {
                doRender(meta.edges.map { edge -> edge.start }, it)
            }
        }
    }
}
