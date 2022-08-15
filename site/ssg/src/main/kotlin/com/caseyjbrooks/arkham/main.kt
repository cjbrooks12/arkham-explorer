package com.caseyjbrooks.arkham

import com.caseyjbrooks.arkham.dag.DependencyGraph
import com.caseyjbrooks.arkham.dag.renderer.LocalServerRenderer
import com.caseyjbrooks.arkham.dag.renderer.StaticOutputRenderer
import com.caseyjbrooks.arkham.site.BuildConfig
import com.caseyjbrooks.arkham.stages.api.ApiData
import com.caseyjbrooks.arkham.stages.assets.CopyOtherAssets
import com.caseyjbrooks.arkham.stages.assets.RasterizeSvgs
import com.caseyjbrooks.arkham.stages.config.ConfigStage
import com.caseyjbrooks.arkham.stages.content.StaticContent
import com.caseyjbrooks.arkham.stages.content.StaticPages
import com.caseyjbrooks.arkham.stages.copyscripts.CopyScripts
import com.caseyjbrooks.arkham.stages.meta.Favicons
import com.caseyjbrooks.arkham.stages.meta.RobotsTxt
import com.caseyjbrooks.arkham.stages.meta.Sitemap
import com.caseyjbrooks.arkham.utils.SiteConfiguration
import kotlinx.coroutines.runBlocking
import kotlin.io.path.Path
import kotlin.io.path.absolute

fun main(): Unit = runBlocking {
    System.setProperty("java.awt.headless", "true")

    val graph = DependencyGraph(
        config = SiteConfiguration(
            rootDir = Path("./../../").absolute().normalize(),
        ),

        ConfigStage(1),
        Favicons(),
        RobotsTxt(),
        Sitemap(),
        StaticContent(),
        StaticPages(),
        RasterizeSvgs(),
        CopyOtherAssets(),
        CopyScripts(),
        ApiData(),

        renderers = buildList {
            if (BuildConfig.DEBUG) {
                this += LocalServerRenderer(8080)
            }
            this += StaticOutputRenderer()
        },
//        updater = if (BuildConfig.DEBUG) {
//            ConstantUpdaterService(10.seconds)
//        } else {
//            ImmediateUpdaterService()
//        },
    )

    graph.executeGraph()
}



