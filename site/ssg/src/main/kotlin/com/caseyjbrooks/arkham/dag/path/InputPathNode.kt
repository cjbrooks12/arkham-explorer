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
    val baseInputDir: Path

    /**
     * This path is relative to [SiteConfiguration.rootDir]
     */
    val inputPath: Path

    fun realInputFile(): Path {
        return baseInputDir / inputPath
    }

    private fun hashFile(graph: DependencyGraph): Path {
        val hash: String = realInputFile().md5
        return graph.config.hashesDir / hash.substring(0, 2) / hash.substring(2, 4) / hash
    }

    override suspend fun preload(graph: DependencyGraph) {
        // nothing to preload, it's already on file and we don't want to load it into memory yet
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
