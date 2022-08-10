package com.caseyjbrooks.arkham.stages.expansiondata.inputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.copperleaf.arkham.models.ArkhamHorrorExpansionsIndex
import io.ktor.client.HttpClient

object ArkhamDbPackCardsApi {
    public val tags = listOf("FetchExpansionData", "input", "cards", "api")

    /**
     * GET https://arkhamdb.com/api/public/packs, to mix additional information for each cycle/pack into those
     * manually configured.
     */
    public suspend fun loadPacksCards(
        scope: DependencyGraphBuilder.Scope,
        packsConfigNode: StartPathNode,
        packsHttpNode: StartHttpNode,
        packConfig: ArkhamHorrorExpansionsIndex.ArkhamHorrorExpansionIndex,
        packCode: String,
        http: HttpClient,
    ): StartHttpNode = with(scope) {
        val packCards = StartHttpNode(
            httpClient = http,
            url = "https://arkhamdb.com/api/public/cards/${packCode}.json",
            tags = tags + listOf(packConfig.slug, packCode),
        )
        addNode(packCards)
        addEdge(packsConfigNode, packCards)
        addEdge(packsHttpNode, packCards)

        return packCards
    }

    public suspend fun getNode(
        scope: DependencyGraphBuilder.Scope,
        packConfig: ArkhamHorrorExpansionsIndex.ArkhamHorrorExpansionIndex,
        packCode: String,
    ): StartHttpNode = with(scope) {
        return graph.getNodeOfType<StartHttpNode> { it.meta.tags == (tags + listOf(packConfig.slug, packCode)) }
    }
}
