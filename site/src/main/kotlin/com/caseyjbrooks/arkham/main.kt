package com.caseyjbrooks.arkham

import com.caseyjbrooks.arkham.site.BuildConfig
import com.caseyjbrooks.arkham.stages.copyscripts.CopyScripts
import com.caseyjbrooks.arkham.stages.expansiondata.FetchExpansionData
import com.caseyjbrooks.arkham.stages.mainpages.GenerateMainPages
import com.caseyjbrooks.arkham.stages.processimages.ProcessImages
import com.caseyjbrooks.arkham.utils.cache.CacheService
import com.caseyjbrooks.arkham.utils.generator.GeneratorService
import com.caseyjbrooks.arkham.utils.index.IndexService
import com.caseyjbrooks.arkham.utils.renderer.RenderService
import com.caseyjbrooks.arkham.utils.resources.ResourceService
import com.caseyjbrooks.arkham.utils.server.LocalServerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.io.path.Path

/**
 * How this thing works:
 *
 * 1. Generators each identify their input files, and map them to outputs (each generator in parallel, they can run their own code in parallel too)
 * 2. Check the Input against the cached hashes
 *      2a. Each Input checks itself to see if it is dirty, setting a dirty flag if so
 * 3. If running in debug, start a local server to serve these entries from memory, render the contents only once requested
 * 4. If running in production:
 *      4a. Go through all output files and pre-create the output directories and files (sequentially, to avoid concurrency issues writing files on disk)
 *      4b. Go back through all real file outputs and render their content into the premade files (each output file in parallel)
 */
fun main(): Unit = runBlocking {
    withContext(Dispatchers.IO) {
        // setup
        val cacheService = CacheService(
            rootDir = Path("./../"),
            outputPath = "build/dist",
            hashesPath = "build/cache/site",
            httpCachePath = "build/cache/http",
        )
        val resourceService = ResourceService(cacheService)
        val generatorService = GeneratorService(
            ProcessImages(cacheService, resourceService),
            FetchExpansionData(cacheService, resourceService),
            CopyScripts(cacheService, resourceService),
            GenerateMainPages(cacheService, resourceService),
        )
        val indexService = IndexService()

        val serverService = LocalServerService(BuildConfig.PORT)
        val renderService = RenderService()

        // build side
        cacheService.createIndex(generatorService, indexService)

        if (BuildConfig.DEBUG) {
            // in debug mode, don't actually write any files to disk. Just serve them via HTTP on localhost
            serverService.startServer(indexService)
        } else {
            // in production mode, write files to disk so that they can be deployed to GitHub Pages
            renderService.renderAllOutputs(cacheService, indexService)
        }
    }
}
