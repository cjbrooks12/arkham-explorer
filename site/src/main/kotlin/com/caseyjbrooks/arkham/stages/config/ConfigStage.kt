package com.caseyjbrooks.arkham.stages.config

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder

class ConfigStage(val version: Int) : DependencyGraphBuilder {
    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        when (graph.currentIteration) {
            1 -> {
                addNode(
                    SiteConfigNode(version)
                )
            }
        }
    }
}
