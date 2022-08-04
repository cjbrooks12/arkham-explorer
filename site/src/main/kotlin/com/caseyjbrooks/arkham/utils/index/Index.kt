package com.caseyjbrooks.arkham.utils.index

import com.caseyjbrooks.arkham.utils.cache.Cacheable
import java.io.OutputStream
import java.nio.file.Paths
import kotlin.io.path.div
import kotlin.io.path.outputStream

class Index(
    val entries: List<IndexEntry<*, *>>
) {

    fun getEntry(path: String): IndexEntry<*, *>? {
        val exactPath = Paths.get(path)
        val indexHtmlPath = exactPath / "index.html"
        // look for an entry with the exact name
        return entries.singleOrNull() { it.output.outputPath == exactPath }
            // look for an entry at the path, but with an index.html file in the directory
            ?: entries.singleOrNull() { it.output.outputPath == indexHtmlPath }
    }
}

data class IndexEntry<T : Cacheable.Input<T, U>, U : Cacheable.Output<T, U>>(
    val input: Cacheable.Input<T, U>,
    val output: Cacheable.Output<T, U>,
    val dirty: Boolean,
) {
    suspend fun renderToOwnOutput() {
        renderToStream(output.realOutput.outputStream())
    }

    suspend fun renderToStream(outputStream: OutputStream) {
        outputStream.use { os ->
            output.render(input as T, os)
        }
    }
}

suspend fun Cacheable.Input<*, *>.createIndexEntries(
    dirty: Boolean
): List<IndexEntry<*, *>> {
    return createRealIndexEntries(dirty)
}

suspend fun <T : Cacheable.Input<T, U>, U : Cacheable.Output<T, U>> Cacheable.Input<T, U>.createRealIndexEntries(
    dirty: Boolean
): List<IndexEntry<T, U>> {
    return outputs().map { output ->
        IndexEntry(
            input = this,
            output = output,
            dirty = dirty
        )
    }
}
