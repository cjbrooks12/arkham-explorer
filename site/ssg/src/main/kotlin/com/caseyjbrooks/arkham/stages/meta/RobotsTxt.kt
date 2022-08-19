package com.caseyjbrooks.arkham.stages.meta

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.site.BuildConfig
import java.nio.file.Paths

@Suppress("BlockingMethodInNonBlockingContext")
class RobotsTxt : DependencyGraphBuilder {

    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        if(graph.currentIteration == 1) {
            addNode(
                TerminalPathNode(
                    baseOutputDir = graph.config.outputDir,
                    outputPath = Paths.get("robots.txt"),
                    doRender = { _, os ->
                        os.write("""
                            |User-agent: *
                            |Disallow: *
                            |Sitemap: ${BuildConfig.BASE_URL}/sitemap.xml
                        """.trimMargin().toByteArray())
                    }
                )
            )
        }
    }
}
