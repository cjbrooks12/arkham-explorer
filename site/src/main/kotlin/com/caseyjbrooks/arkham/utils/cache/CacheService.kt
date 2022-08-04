package com.caseyjbrooks.arkham.utils.cache

import com.caseyjbrooks.arkham.utils.findDuplicates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.div
import kotlin.io.path.isRegularFile
import kotlin.io.path.outputStream
import kotlin.streams.toList

/**
 * How this thing works:
 *
 * 1. Generators each identify their input files, and map them to outputs (all in parallel)
 * 2. Check the Input against the cached hashes
 *     2a. Continue with generation if the input hash has changed
 *     2b. If the input hash is the same, check the outputs. If the outputs have changed, continue with generation
 * 3. Go through all output files and pre-create the output (sequentially)
 * 4. Go back through all real file outputs and let the generator render content into the premade files (all in parallel)
 */

@Suppress("RedundantIf")
class CacheService(
    val rootDir: Path,
    val outputDir: Path,
    private val hashesDir: Path,
    private val httpCacheDir: Path,
) {
    private data class IndexEntry<T : Cacheable.Input<T, U>, U : Cacheable.Output<T, U>>(
        val input: Cacheable.Input<T, U>,
        val output: Cacheable.Output<T, U>,
        val dirty: Boolean,
    ) {
        suspend fun renderSelf() {
            renderToStream(output.realOutput.outputStream())
        }

        suspend fun renderToStream(outputStream: OutputStream) {
            outputStream.use { os ->
                output.render(input as T, os)
            }
        }
    }

    private suspend fun Cacheable.Input<*, *>.createIndexEntries(
    ): List<IndexEntry<*, *>> {
        return createRealIndexEntries()
    }

    private suspend fun <T : Cacheable.Input<T, U>, U : Cacheable.Output<T, U>> Cacheable.Input<T, U>.createRealIndexEntries(
    ): List<IndexEntry<T, U>> {
        val dirty = dirty(hashesDir)
        return outputs().map { output ->
            IndexEntry(
                input = this,
                output = output,
                dirty = dirty
            )
        }
    }

    private var index: List<IndexEntry<*, *>> = emptyList()

    /**
     * List files in the directory non-recursively. The returned paths are relative to the repo root. Only regular files
     * will be returned (no directories).
     */
    suspend fun getFilesInDir(path: String): List<Path> = withContext(Dispatchers.IO) {
        Files
            .list(rootDir / path)
            .filter { it.isRegularFile() }
            .map { rootDir.relativize(it) }
            .toList()
    }

    /**
     * List files in the directory recursively. The returned paths are relative to the repo root. Only regular files
     * will be returned (no directories).
     */
    suspend fun getFilesInDirs(path: String): List<Path> = withContext(Dispatchers.IO) {
        Files
            .walk(rootDir / path)
            .filter { it.isRegularFile() }
            .map { rootDir.relativize(it) }
            .toList()
    }

    suspend fun createIndex(allInputs: Iterable<Cacheable.Input<*, *>>) {
        index = allInputs
            .flatMap { input ->
                input.createIndexEntries()
            }
    }

    suspend fun verifyIndex() {
        val duplicates = index
            .map { it.output.realOutput }
            .findDuplicates()
        check(duplicates.isEmpty()) {
            "Multiple entries are attempting to create the same file: $duplicates"
        }
    }

    suspend fun prepareOutputDirectory() {
        index
            .filter { it.dirty }
            .forEach { indexEntry ->
                indexEntry.output.realOutput.parent.createDirectories()
                indexEntry.output.realOutput.deleteIfExists()
                indexEntry.output.realOutput.createFile()
            }
    }

    suspend fun renderAllOutputs() = coroutineScope {
        val mutex = Mutex()
        index
            .filter { it.dirty }
            .also { println("Rendering ${it.size} outputs (${index.size - it.size} skipped)") }
            .map { indexEntry ->
                async {
                    // render to the prepared output file
                    indexEntry.renderSelf()

                    mutex.withLock {
                        indexEntry.input.writeHash(hashesDir)
                    }
                }
            }
            .awaitAll()
    }

    suspend fun renderFilePathExternally(path: String, os: OutputStream) {
        val comparePath = Paths.get(path)
        index
            .single { it.output.outputPath == comparePath }
            .also { println("Returning $path") }
            .renderToStream(os)
    }
}
