package com.caseyjbrooks.arkham.stages.api.inputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.Node
import com.caseyjbrooks.arkham.dag.http.InputHttpNode
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbCard
import io.ktor.client.HttpClient
import kotlinx.serialization.builtins.ListSerializer

object ArkhamDbPackCardsApi {
    public val tags = listOf("FetchExpansionData", "input", "cards", "api")

    public suspend fun loadPacksCards(
        scope: DependencyGraphBuilder.Scope,
        packsApiNode: InputHttpNode, // https://arkhamdb.com/api/public/packs response
        packCode: String,
        http: HttpClient,
    ): InputHttpNode = with(scope) {
        val packCards = StartHttpNode(
            httpClient = http,
            url = "https://arkhamdb.com/api/public/cards/${packCode}.json",
            tags = tags + packCode,
        )
        addNode(packCards)
        addEdge(packsApiNode, packCards)

        return packCards
    }

    public suspend fun getNode(
        scope: DependencyGraphBuilder.Scope,
        packCode: String,
    ): InputHttpNode = with(scope) {
        return graph.getNodeOfType<StartHttpNode> { it.meta.tags == (tags + packCode) }
    }

    public suspend fun getNodes(
        scope: DependencyGraphBuilder.Scope,
    ): List<InputHttpNode> = with(scope) {
        return graph.getNodesOfType<StartHttpNode> { it.meta.tags.containsAll(tags) }
    }

    public suspend fun getNodesForOutput(
        nodes: List<Node>,
    ): List<InputHttpNode> {
        return nodes.filterIsInstance<StartHttpNode>().filter { it.meta.tags.containsAll(tags) }
    }

    public suspend fun getBody(
        scope: DependencyGraphBuilder.Scope,
        node: InputHttpNode,
    ): List<ArkhamDbCard> {
        return node.getResponse(scope.graph, ListSerializer(ArkhamDbCard.serializer()))
    }

    public suspend fun getCachedBody(
        scope: DependencyGraphBuilder.Scope,
        packCode: String,
    ): Pair<InputHttpNode, List<ArkhamDbCard>> {
        val node = getNode(scope, packCode)
        return node to getBody(scope, node)
    }
}
