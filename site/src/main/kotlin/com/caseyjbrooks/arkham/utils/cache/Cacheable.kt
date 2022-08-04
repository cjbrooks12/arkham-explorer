package com.caseyjbrooks.arkham.utils.cache

import java.io.OutputStream
import java.nio.file.Path

object Cacheable {
    interface Input<T : Cacheable.Input<T, U>, U : Cacheable.Output<T, U>> {
        val outputs: suspend () -> List<U>

        fun dirty(hashesDir: Path): Boolean
        fun writeHash(hashesDir: Path)
    }

    interface Output<T : Cacheable.Input<T, U>, U : Cacheable.Output<T, U>> {
        val outputPath: Path
        val realOutput: Path
        val render: suspend (T, OutputStream) -> Unit
    }
}
