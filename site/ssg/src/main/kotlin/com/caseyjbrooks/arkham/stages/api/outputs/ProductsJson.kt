package com.caseyjbrooks.arkham.stages.api.outputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.InputHttpNode
import com.caseyjbrooks.arkham.dag.http.prettyJson
import com.caseyjbrooks.arkham.dag.path.InputPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.stages.api.inputs.ArkhamDbPacksApi
import com.caseyjbrooks.arkham.stages.api.inputs.LocalExpansionFile
import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPack
import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion
import com.caseyjbrooks.arkham.stages.api.utils.asFullOutput
import com.copperleaf.arkham.models.api.ProductList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Paths

object ProductsJson {
    public val tags = listOf("FetchExpansionData", "output", "products")

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        local: List<InputPathNode>,
        packsHttpNode: InputHttpNode,
    ): TerminalPathNode = with(scope) {
        val productsOutputNode = TerminalPathNode(
            tags = tags,
            baseOutputDir = graph.config.outputDir,
            outputPath = Paths.get("api/products.json"),
            doRender = { nodes, os ->
                val localExpansionFiles = LocalExpansionFile.getBodiesForOutput(scope, nodes).map { it.second }
                val packsApi = ArkhamDbPacksApi.getBodyForOutput(scope, nodes)
                prettyJson.encodeToStream(
                    ProductList.serializer(),
                    createJson(localExpansionFiles, packsApi),
                    os,
                )
            },
        )
        addNode(productsOutputNode)
        local.forEach { localExpansionNode ->
            addEdge(localExpansionNode, productsOutputNode)
        }
        addEdge(packsHttpNode, productsOutputNode)

        return productsOutputNode
    }

    private fun createJson(
        localExpansions: List<LocalArkhamHorrorExpansion>,
        packsApi: List<ArkhamDbPack>,
    ): ProductList {
        return ProductList(
            products = localExpansions
                .flatMap { localExpansion ->
                    localExpansion
                        .products
                        .map { it.asFullOutput(localExpansion.code, localExpansions, packsApi) }
                        .sortedBy { it.id }
                }
        )
    }
}
