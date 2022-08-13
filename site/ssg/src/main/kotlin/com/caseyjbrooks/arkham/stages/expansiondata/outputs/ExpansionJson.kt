package com.caseyjbrooks.arkham.stages.expansiondata.outputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.InputHttpNode
import com.caseyjbrooks.arkham.dag.http.prettyJson
import com.caseyjbrooks.arkham.dag.path.InputPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.stages.expansiondata.inputs.ArkhamDbPacksApi
import com.caseyjbrooks.arkham.stages.expansiondata.inputs.LocalExpansionFile
import com.caseyjbrooks.arkham.stages.expansiondata.inputs.models.ArkhamDbPack
import com.caseyjbrooks.arkham.stages.expansiondata.inputs.models.LocalArkhamHorrorExpansion
import com.caseyjbrooks.arkham.stages.expansiondata.utils.asFullOutput
import com.copperleaf.arkham.models.api.Expansion
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Paths

object ExpansionJson {
    public val tags = listOf("FetchExpansionData", "output", "expansion")

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        localExpansionFiles: List<InputPathNode>,
        expansionCode: String,
        packsHttpNode: InputHttpNode,
        packHttpNodes: List<InputHttpNode>,
    ): TerminalPathNode = with(scope) {
        val expansionNode = TerminalPathNode(
            baseOutputDir = graph.config.outputDir,
            outputPath = Paths.get("api/expansions/$expansionCode.json"),
            doRender = { nodes, os ->
                val localExpansions = LocalExpansionFile.getBodiesForOutput(scope, nodes).map { it.second }
                val localExpansion = LocalExpansionFile.getBodyForOutput(scope, nodes, expansionCode)
                val packsApi = ArkhamDbPacksApi.getBodyForOutput(scope, nodes)
                prettyJson.encodeToStream(
                    Expansion.serializer(),
                    createJson(
                        expansionCode,
                        localExpansions,
                        localExpansion,
                        packsApi,
                    ),
                    os,
                )
            },
            tags = tags + expansionCode
        )
        addNode(expansionNode)
        localExpansionFiles.forEach { addEdge(it, expansionNode) }
        addEdge(packsHttpNode, expansionNode)

        return expansionNode
    }

    private fun createJson(
        expansionCode: String,
        localExpansions: List<LocalArkhamHorrorExpansion>,
        localExpansion: LocalArkhamHorrorExpansion,
        packsApi: List<ArkhamDbPack>,
    ): Expansion {
        return localExpansion.asFullOutput(expansionCode, localExpansions, packsApi)
    }
}
