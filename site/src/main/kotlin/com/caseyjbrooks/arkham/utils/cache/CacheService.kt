package com.caseyjbrooks.arkham.utils.cache

import com.caseyjbrooks.arkham.utils.findDuplicates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.bufferedReader
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import kotlin.io.path.outputStream
import kotlin.io.path.writeText
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
            output.realOutput.outputStream().use { os ->
                output.render(input as T, os)
            }
        }
    }

    private suspend fun Cacheable.Input<*, *>.createIndexEntries(
        dirty: Boolean
    ): List<IndexEntry<*, *>> {
        return createRealIndexEntries(dirty)
    }

    private suspend fun <T : Cacheable.Input<T, U>, U : Cacheable.Output<T, U>> Cacheable.Input<T, U>.createRealIndexEntries(
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

                val dirty = if (input.hashFile.exists()) {
                    // the file hasn't changed, but check the version in that file
                    val cachedVersion = input.hashFile.bufferedReader().readText()
                    if (cachedVersion.toLong() == input.version) {
                        // the cache is fine, no need to re-render anything from this file
                        false
                    } else {
                        // the input hasn't changed, but the generator code has. We want to re-render it
                        true
                    }
                } else {
                    // the hash changed, we want to re-render it
                    true
                }

                input.createIndexEntries(dirty)
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
                        // write the hash and version to file to skip this file next run
                        val hashFile = indexEntry.input.hashFile

                        val shouldWriteHashFile = if (hashFile.exists()) {
                            val cachedVersion = hashFile.bufferedReader().readText()

                            if (cachedVersion.toLong() == indexEntry.input.version) {
                                false
                            } else {
                                true
                            }
                        } else {
                            true
                        }

                        if (shouldWriteHashFile) {
                            hashFile.parent.createDirectories()
                            hashFile.deleteIfExists()
                            hashFile.createFile()
                            hashFile.writeText(indexEntry.input.version.toString())
                        }
                    }
                }
            }
            .awaitAll()
    }

    private val Cacheable.Input<*, *>.hashFile: Path get() = hashesDir / hash.substring(0, 2) / hash.substring(2, 4) / "$hash-$processor"
}
