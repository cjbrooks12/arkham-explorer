package com.caseyjbrooks.arkham.stages.expansiondata.inputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.copperleaf.arkham.models.ArkhamHorrorExpansionsIndex
import java.nio.file.Paths

object LocalExpansionFile {
    public val tags = listOf("FetchExpansionData", "input", "expansion", "local")

    /**
     * GET https://arkhamdb.com/api/public/packs, to mix additional information for each cycle/pack into those
     * manually configured.
     */
    public suspend fun loadExpansionFile(
        scope: DependencyGraphBuilder.Scope,
        packsConfigNode: StartPathNode,
        packsHttpNode: StartHttpNode,
        packConfig: ArkhamHorrorExpansionsIndex.ArkhamHorrorExpansionIndex,
    ): StartPathNode = with(scope) {
        val expansionInputFileNode = StartPathNode(
            baseInputDir = graph.config.dataDir,
            inputPath = Paths.get("${packConfig.slug}.json"),
            tags = tags + packConfig.slug,
        )
        addNode(expansionInputFileNode)
        addEdge(packsConfigNode, expansionInputFileNode)
        addEdge(packsHttpNode, expansionInputFileNode)

        return expansionInputFileNode
    }

    public suspend fun getNode(
        scope: DependencyGraphBuilder.Scope,
        packConfig: ArkhamHorrorExpansionsIndex.ArkhamHorrorExpansionIndex,
    ): StartPathNode = with(scope) {
        return graph.getNodeOfType<StartPathNode> { it.meta.tags == (tags + packConfig.slug) }
    }
}
