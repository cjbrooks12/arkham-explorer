package com.caseyjbrooks.arkham.stages.meta

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import java.nio.file.Paths

class Favicons : DependencyGraphBuilder {

    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        if(graph.currentIteration == 1) {
            addNode(
                TerminalPathNode(
                    baseOutputDir = graph.config.outputDir,
                    outputPath = Paths.get("favicon.ico"),
                    doRender = { nodes, os ->
                        os.write(ByteArray(0))
                    }
                )
            )
        }
    }
}
