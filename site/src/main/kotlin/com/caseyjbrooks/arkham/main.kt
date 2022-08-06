package com.caseyjbrooks.arkham

import com.caseyjbrooks.arkham.dag.DependencyGraph
import com.caseyjbrooks.arkham.dag.renderer.LocalServerRenderer
import com.caseyjbrooks.arkham.dag.renderer.StaticOutputRenderer
import com.caseyjbrooks.arkham.site.BuildConfig
import com.caseyjbrooks.arkham.stages.config.ConfigStage
import com.caseyjbrooks.arkham.stages.copyscripts.CopyScripts
import com.caseyjbrooks.arkham.stages.expansiondata.FetchExpansionData
import com.caseyjbrooks.arkham.stages.mainpages.GenerateMainPages
import com.caseyjbrooks.arkham.stages.processimages.ProcessImages
import com.caseyjbrooks.arkham.utils.SiteConfiguration
import kotlinx.coroutines.runBlocking
import kotlin.io.path.Path

fun main(): Unit = runBlocking {
    val graph = DependencyGraph(
        config = SiteConfiguration(
            rootDir = Path("./../"),
            outputPath = "build/dist",
            hashesPath = "build/cache/site",
            httpCachePath = "build/cache/http",
        ),

        ConfigStage(1),
        GenerateMainPages(),
        ProcessImages(),
        CopyScripts(),
        FetchExpansionData(),

        renderer = if(BuildConfig.DEBUG) {
            LocalServerRenderer(8080)
        } else {
            StaticOutputRenderer()
        },
    )

    graph.executeGraph()
}



