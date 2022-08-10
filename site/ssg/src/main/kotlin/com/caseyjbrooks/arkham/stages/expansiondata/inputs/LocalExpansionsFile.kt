package com.caseyjbrooks.arkham.stages.expansiondata.inputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import java.nio.file.Paths

object LocalExpansionsFile {
    public val tags = listOf("FetchExpansionData", "input", "expansions", "local")

    /**
     * A local index of packs that weill ultimately serve to tell the SPA which packs are available
     */
    public suspend fun loadExpansionsFile(
        scope: DependencyGraphBuilder.Scope,
        siteConfigNode: SiteConfigNode,
    ): StartPathNode = with(scope) {
        addNodeAndEdge(
            start = siteConfigNode,
            newEndNode = StartPathNode(
                baseInputDir = graph.config.dataDir,
                inputPath = Paths.get("expansions.json"),
                tags = tags,
            )
        )
    }

    public suspend fun getNode(
        scope: DependencyGraphBuilder.Scope,
    ): StartPathNode = with(scope) {
        return graph.getNodeOfType<StartPathNode> { it.meta.tags == tags }
    }
}
