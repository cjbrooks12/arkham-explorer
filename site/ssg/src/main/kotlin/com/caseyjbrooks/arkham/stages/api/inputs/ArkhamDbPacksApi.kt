package com.caseyjbrooks.arkham.stages.api.inputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.Node
import com.caseyjbrooks.arkham.dag.http.InputHttpNode
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPack
import io.ktor.client.HttpClient
import kotlinx.serialization.builtins.ListSerializer

object ArkhamDbPacksApi {
    public val tags = listOf("FetchExpansionData", "input", "packs", "api")

    /**
     * GET https://arkhamdb.com/api/public/packs, to mix additional information for each cycle/pack into those
     * manually configured.
     */
    public suspend fun loadPacksList(
        scope: DependencyGraphBuilder.Scope,
        siteConfigNode: SiteConfigNode,
        http: HttpClient,
    ): InputHttpNode = with(scope) {
        addNodeAndEdge(
            start = siteConfigNode,
            newEndNode = StartHttpNode(
                httpClient = http,
                url = "https://arkhamdb.com/api/public/packs",
                tags = tags,
            )
        )
    }

    public suspend fun getNode(
        scope: DependencyGraphBuilder.Scope,
    ): InputHttpNode = with(scope) {
        return graph.getNodeOfType<StartHttpNode> { it.meta.tags == tags }
    }

    public suspend fun getBodyForOutput(
        scope: DependencyGraphBuilder.Scope,
        nodes: List<Node>,
    ): List<ArkhamDbPack> {
        return nodes
            .filterIsInstance<StartHttpNode>()
            .single { it.meta.tags == tags }
            .let { getBody(scope, it) }
    }

    public suspend fun getBody(
        scope: DependencyGraphBuilder.Scope,
        node: InputHttpNode,
    ): List<ArkhamDbPack> {
        return node.getResponse(scope.graph, ListSerializer(ArkhamDbPack.serializer()))
    }

    public suspend fun getCachedBody(
        scope: DependencyGraphBuilder.Scope,
    ): Pair<InputHttpNode, List<ArkhamDbPack>> {
        val node = getNode(scope)
        return node to getBody(scope, node)
    }
}
