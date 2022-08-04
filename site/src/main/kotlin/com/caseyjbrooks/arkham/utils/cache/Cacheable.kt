package com.caseyjbrooks.arkham.utils.cache

import com.caseyjbrooks.arkham.utils.md5
import java.io.OutputStream
import java.nio.file.Path
import kotlin.io.path.bufferedReader
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.readBytes
import kotlin.io.path.writeText

object Cacheable {
    sealed interface Input<T : Cacheable.Input<T, U>, U : Cacheable.Output<T, U>> {
        val outputs: suspend () -> List<U>

        fun dirty(hashesDir: Path): Boolean
        fun writeHash(hashesDir: Path)
    }

    sealed interface Output<T : Cacheable.Input<T, U>, U : Cacheable.Output<T, U>> {
        val outputPath: Path
        val realOutput: Path
        val render: suspend (T, OutputStream) -> Unit
    }

// Path-based Input
// ---------------------------------------------------------------------------------------------------------------------

    data class InputPath(
        private val processor: String,
        private val version: Long,
        val rootDir: java.nio.file.Path,
        val inputPath: java.nio.file.Path,
        override val outputs: suspend () -> List<OutputFromPath>,
    ) : Cacheable.Input<InputPath, OutputFromPath> {
        val realInput: java.nio.file.Path = rootDir / inputPath
        val hash: String = realInput.md5

        private fun hashFile(hashesDir: java.nio.file.Path): java.nio.file.Path =
            hashesDir / hash.substring(0, 2) / hash.substring(2, 4) / "$hash-$processor"

        override fun dirty(hashesDir: java.nio.file.Path): Boolean {
            val hashFile = hashFile(hashesDir)
            return if (hashFile.exists()) {
                // the file hasn't changed, but check the version in that file
                val cachedVersion = hashFile.bufferedReader().readText()
                if (cachedVersion.toLong() == version) {
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
        }

        override fun writeHash(hashesDir: java.nio.file.Path) {
            // write the hash and version to file to skip this file next run
            val hashFile = hashFile(hashesDir)

            if (dirty(hashesDir)) {
                hashFile.parent.createDirectories()
                hashFile.deleteIfExists()
                hashFile.createFile()
                hashFile.writeText(version.toString())
            }
        }
    }

    data class OutputFromPath(
        val outputDir: Path,
        override val outputPath: Path,
        override val render: suspend (InputPath, OutputStream) -> Unit = { input, os ->
            os.use {
                it.write(input.realInput.readBytes())
            }
        },
    ) : Cacheable.Output<InputPath, OutputFromPath> {
        override val realOutput: Path = outputDir / outputPath
    }

}
