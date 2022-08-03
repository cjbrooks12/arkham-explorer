package com.caseyjbrooks.arkham

import com.caseyjbrooks.arkham.stages.copyscripts.CopyScripts
import com.caseyjbrooks.arkham.stages.mainpages.GenerateMainPages
import com.caseyjbrooks.arkham.stages.processimages.ProcessImages
import com.caseyjbrooks.arkham.utils.cache.CacheService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.io.path.Path
import kotlin.io.path.div

fun main(): Unit = runBlocking {
    withContext(Dispatchers.IO) {
        val repoRoot = Path("./../")
        val cacheService = CacheService(
            rootDir = repoRoot,
            outputDir = repoRoot / "build/dist",
            hashesDir = repoRoot / "build/cache/site",
            httpCacheDir = repoRoot / "build/cache/http",
        )

        val stages = listOf(
            ProcessImages(cacheService),
//            FetchExpansionData(cacheService),
            CopyScripts(cacheService),
            GenerateMainPages(cacheService),
        )

        val allInputs = stages
            .map { async { it.process() } }
            .awaitAll()
            .flatten()

        cacheService.createIndex(allInputs)
        cacheService.verifyIndex()
        cacheService.prepareOutputDirectory()
        cacheService.renderAllOutputs()
    }
}
