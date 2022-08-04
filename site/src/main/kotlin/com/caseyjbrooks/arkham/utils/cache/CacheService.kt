package com.caseyjbrooks.arkham.utils.cache

import com.caseyjbrooks.arkham.utils.generator.GeneratorService
import com.caseyjbrooks.arkham.utils.index.Index
import com.caseyjbrooks.arkham.utils.index.IndexService
import com.caseyjbrooks.arkham.utils.index.createIndexEntries
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.div

class CacheService(
    val rootDir: Path,
    val outputDir: Path,
    private val hashesDir: Path,
    private val httpCacheDir: Path,
) {
    constructor(
        rootDir: Path,
        outputPath: String,
        hashesPath: String,
        httpCachePath: String,
    ) : this(
        rootDir = rootDir,
        outputDir = rootDir / outputPath,
        hashesDir = rootDir / hashesPath,
        httpCacheDir = rootDir / httpCachePath,
    )

    suspend fun createIndex(
        generatorService: GeneratorService,
        indexService: IndexService
    ) {
        indexService.index = Index(
            generatorService
                .getInputEntries()
                .flatMap { input ->
                    val dirty = input.dirty(hashesDir)
                    input.createIndexEntries(dirty)
                }
        )
    }

    suspend fun prepareOutputDirectories(indexService: IndexService) {
        indexService
            .index
            .entries
            .filter { it.dirty }
            .forEach { indexEntry ->
                indexEntry.output.realOutput.parent.createDirectories()
                indexEntry.output.realOutput.deleteIfExists()
                indexEntry.output.realOutput.createFile()
            }
    }

    suspend fun renderOutputFiles(indexService: IndexService) = coroutineScope {
        val mutex = Mutex()
        indexService
            .index
            .entries
            .filter { it.dirty }
            .also { println("Rendering ${it.size} outputs (${indexService.index.entries.size - it.size} skipped)") }
            .map { indexEntry ->
                async {
                    // render to the prepared output file
                    indexEntry.renderToOwnOutput()

                    mutex.withLock {
                        indexEntry.input.writeHash(hashesDir)
                    }
                }
            }
            .awaitAll()
    }
}
