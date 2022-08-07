package com.caseyjbrooks.arkham.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.isRegularFile
import kotlin.streams.toList

class ResourceService {

    /**
     * List files in the directory non-recursively. The returned paths are relative to the [baseDir]. Only regular files
     * will be returned (no directories).
     */
    suspend fun getFile(baseDir: Path, fileName: Path): Path = withContext(Dispatchers.IO) {
        val actualPath = baseDir / fileName
        baseDir.relativize(actualPath)
    }

    /**
     * List files in the directory non-recursively. The returned paths are relative to the [baseDir]. Only regular files
     * will be returned (no directories).
     */
    suspend fun getFilesInDir(baseDir: Path): List<Path> = withContext(Dispatchers.IO) {
        Files
            .list(baseDir)
            .use {
                it.filter { it.isRegularFile() }
                    .map { baseDir.relativize(it) }
                    .toList()
            }
    }

    /**
     * List files in the directory recursively. The returned paths are relative to the [baseDir]. Only regular files
     * will be returned (no directories).
     */
    suspend fun getFilesInDirs(baseDir: Path): List<Path> = withContext(Dispatchers.IO) {
        Files
            .walk(baseDir)
            .use {
                it
                    .filter { it.isRegularFile() }
                    .map { baseDir.relativize(it) }
                    .toList()
            }
    }
}
