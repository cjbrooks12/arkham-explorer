package com.caseyjbrooks.arkham.stages.expansiondata.outputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.copperleaf.arkham.models.ArkhamHorrorExpansionsIndex
import java.nio.file.Paths
import kotlin.io.path.div

object ExpansionProductsJson {
    public val tags = listOf("FetchExpansionData", "output", "expansion", "products")

    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        packsConfigNode: StartPathNode,
        packsHttpNode: StartHttpNode,
        localExpansionFile: StartPathNode,
        packConfig: ArkhamHorrorExpansionsIndex.ArkhamHorrorExpansionIndex,
    ): TerminalPathNode = with(scope) {
        val node = TerminalPathNode(
            baseOutputDir = graph.config.outputDir / "api/expansions",
            outputPath = Paths.get("${packConfig.slug}/products.json"),
            doRender = { nodes, os ->
                os.write("{}".toByteArray())
            },
            tags = ExpansionJson.tags + packConfig.slug
        )
        addNode(node)
        addEdge(packsConfigNode, node)
        addEdge(packsHttpNode, node)
        addEdge(localExpansionFile, node)

        return node
    }
}
