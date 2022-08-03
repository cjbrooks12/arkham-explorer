package com.caseyjbrooks.arkham.stages.copyscripts

import com.caseyjbrooks.arkham.stages.ProcessingStage
import com.caseyjbrooks.arkham.utils.cache.CacheService
import com.caseyjbrooks.arkham.utils.cache.Cacheable
import java.nio.file.Paths
import kotlin.io.path.name

class CopyScripts(
    private val cacheService: CacheService
) : ProcessingStage {
    companion object {
        private const val VERSION = 1L
    }

    override suspend fun process(): Iterable<Cacheable.Input<*, *>> {
        return cacheService
            .getFilesInDir("app/build/distributions")
            .filterNot { it.name == "index.html" }
            .map { inputPath ->
                Cacheable.Input.CachedPath(
                    processor = "CopyScripts",
                    version = CopyScripts.VERSION,
                    inputPath = inputPath,
                    rootDir = cacheService.rootDir,
                    outputs = {
                        listOf(
                            Cacheable.Output.CachedPath(
                                outputDir = cacheService.outputDir,
                                outputPath = Paths.get("app/build/distributions").relativize(inputPath),
                            )
                        )
                    }
                )
            }
    }
}
