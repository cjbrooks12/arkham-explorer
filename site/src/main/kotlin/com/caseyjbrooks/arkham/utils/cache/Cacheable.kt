package com.caseyjbrooks.arkham.utils.cache

import com.caseyjbrooks.arkham.utils.md5
import java.io.OutputStream
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.readBytes

object Cacheable {
    sealed interface Input<T: Cacheable.Input<T, U>, U: Cacheable.Output<T, U>> {
        val processor: String
        val version: Long
        val hash: String
        val outputs: suspend () -> List<U>

        data class CachedPath(
            override val processor: String,
            override val version: Long,
            val rootDir: Path,
            val inputPath: Path,
            override val outputs: suspend () -> List<Output.CachedPath>,
        ) : Cacheable.Input<Cacheable.Input.CachedPath, Cacheable.Output.CachedPath> {
            val realInput: Path = rootDir / inputPath
            override val hash: String = realInput.md5
        }

//        data class CachedHttpRequest(
//            override val version: Long,
//            val inputUrl: String,
//            override val outputs: () -> List<Output>,
//        ) : Cacheable.Input {
//            override val hash: String = inputUrl.md5
//        }
    }

    sealed interface Output<T: Cacheable.Input<T, U>, U: Cacheable.Output<T, U>> {
        val outputPath: Path
        val realOutput: Path
        val render: suspend (T, OutputStream) -> Unit

        data class CachedPath(
            val outputDir: Path,
            override val outputPath: Path,
            override val render: suspend (Cacheable.Input.CachedPath, OutputStream) -> Unit = { input, os ->
                os.use {
                    it.write(input.realInput.readBytes())
                }
            },
        ) : Cacheable.Output<Cacheable.Input.CachedPath, Cacheable.Output.CachedPath> {
            override val realOutput: Path = outputDir / outputPath
        }
    }
}
