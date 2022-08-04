package com.caseyjbrooks.arkham.utils.resources

import com.caseyjbrooks.arkham.utils.cache.CacheService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.isRegularFile
import kotlin.streams.toList

class ResourceService(private val cacheService: CacheService) {

    /**
     * List files in the directory non-recursively. The returned paths are relative to the repo root. Only regular files
     * will be returned (no directories).
     */
    suspend fun getFilesInDir(path: String): List<Path> = withContext(Dispatchers.IO) {
        Files
            .list(cacheService.rootDir / path)
            .filter { it.isRegularFile() }
            .map { cacheService.rootDir.relativize(it) }
            .toList()
    }

    /**
     * List files in the directory recursively. The returned paths are relative to the repo root. Only regular files
     * will be returned (no directories).
     */
    suspend fun getFilesInDirs(path: String): List<Path> = withContext(Dispatchers.IO) {
        Files
            .walk(cacheService.rootDir / path)
            .filter { it.isRegularFile() }
            .map { cacheService.rootDir.relativize(it) }
            .toList()
    }
}
