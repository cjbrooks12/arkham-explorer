package com.caseyjbrooks.arkham.stages.meta

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import java.nio.file.Paths

class PwaManifest : DependencyGraphBuilder {

    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        if(graph.currentIteration == 1) {
            addNode(
                TerminalPathNode(
                    baseOutputDir = graph.config.outputDir,
                    outputPath = Paths.get("manifest.json"),
                    doRender = { nodes, os ->
                        os.write(
                            """
                            |{
                            |    "short_name": "Arkham",
                            |    "name": "Arkham Explorer",
                            |    "icons": [
                            |        {
                            |            "src": "/assets/night-of-the-zealot-128px.png",
                            |            "type": "image/png",
                            |            "sizes": "128x128"
                            |        },
                            |        {
                            |            "src": "/assets/night-of-the-zealot-512px.png",
                            |            "type": "image/png",
                            |            "sizes": "512x512"
                            |        }
                            |    ],
                            |    "start_url": "/?source=pwa",
                            |    "background_color": "#3367D6",
                            |    "display": "standalone",
                            |    "scope": "/",
                            |    "theme_color": "#3367D6"
                            |}
                            """.trimMargin().toByteArray()
                        )
                    }
                )
            )
        }
    }
}
