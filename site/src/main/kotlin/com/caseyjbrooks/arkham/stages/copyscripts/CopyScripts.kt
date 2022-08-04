package com.caseyjbrooks.arkham.stages.copyscripts

import com.caseyjbrooks.arkham.stages.ProcessingStage
import com.caseyjbrooks.arkham.utils.cache.CacheService
import com.caseyjbrooks.arkham.utils.cache.Cacheable
import com.caseyjbrooks.arkham.utils.resources.ResourceService
import java.nio.file.Paths
import kotlin.io.path.name

class CopyScripts(
    private val cacheService: CacheService,
    private val resourceService: ResourceService,
) : ProcessingStage {
    companion object {
        private const val VERSION = 1L
    }

    override suspend fun process(): Iterable<Cacheable.Input<*, *>> {
        return resourceService
            .getFilesInDir("app/build/distributions")
            .filterNot { it.name == "index.html" }
            .map { inputPath ->
                Cacheable.InputPath(
                    processor = "CopyScripts",
                    version = VERSION,
                    inputPath = inputPath,
                    rootDir = cacheService.rootDir,
                    outputs = {
                        listOf(
                            Cacheable.OutputFromPath(
                                outputDir = cacheService.outputDir,
                                outputPath = Paths.get("app/build/distributions").relativize(inputPath),
                            )
                        )
                    }
                )
            }
    }
}
