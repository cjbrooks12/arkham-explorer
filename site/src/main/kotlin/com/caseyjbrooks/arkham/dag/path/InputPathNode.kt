package com.caseyjbrooks.arkham.dag.path

import com.caseyjbrooks.arkham.dag.DependencyGraph
import com.caseyjbrooks.arkham.dag.Node
import com.caseyjbrooks.arkham.utils.SiteConfiguration
import com.caseyjbrooks.arkham.utils.md5
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.div
import kotlin.io.path.exists

interface InputPathNode : Node.Input {
    /**
     * This path is relative to [SiteConfiguration.rootDir]
     */
    val inputPath: Path

    fun realInputFile(graph: DependencyGraph): Path {
        return graph.config.rootDir / inputPath
    }

    private fun hashFile(graph: DependencyGraph): Path {
        val hash: String = realInputFile(graph).md5
        return graph.config.hashesDir / hash.substring(0, 2) / hash.substring(2, 4) / hash
    }

    override suspend fun dirty(graph: DependencyGraph): Boolean {
        return !hashFile(graph).exists()
    }

    override suspend fun markClean(graph: DependencyGraph) {
        hashFile(graph).apply {
            parent.createDirectories()
            deleteIfExists()
            createFile()
        }
    }
}
