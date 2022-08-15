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
import com.caseyjbrooks.arkham.stages.api.outputs.utils.asFullOutput
import com.copperleaf.arkham.models.api.InvestigatorList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Paths

object InvestigatorsJson {
    public val tags = listOf("FetchExpansionData", "output", "investigators")

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        local: List<InputPathNode>,
        packsHttpNode: InputHttpNode,
    ): TerminalPathNode = with(scope) {
        val investigatorsOutputNode = TerminalPathNode(
            tags = tags,
            baseOutputDir = graph.config.outputDir,
            outputPath = Paths.get("api/investigators.json"),
            doRender = { nodes, os ->
                val localExpansionFiles = LocalExpansionFile.getBodiesForOutput(scope, nodes).map { it.second }
                val packsApi = ArkhamDbPacksApi.getBodyForOutput(scope, nodes)
                prettyJson.encodeToStream(
                    InvestigatorList.serializer(),
                    createJson(localExpansionFiles, packsApi),
                    os,
                )
            },
        )
        addNode(investigatorsOutputNode)
        local.forEach { localExpansionNode ->
            addEdge(localExpansionNode, investigatorsOutputNode)
        }
        addEdge(packsHttpNode, investigatorsOutputNode)

        return investigatorsOutputNode
    }

    private fun createJson(
        localExpansions: List<LocalArkhamHorrorExpansion>,
        packsApi: List<ArkhamDbPack>,
    ): InvestigatorList {
        return InvestigatorList(
            investigators = localExpansions
                .flatMap { localExpansion ->
                    localExpansion
                        .investigators
                        .map { it.asFullOutput(localExpansion.code, localExpansions, packsApi) }
                        .sortedBy { it.id }
                }
        )
    }
}
