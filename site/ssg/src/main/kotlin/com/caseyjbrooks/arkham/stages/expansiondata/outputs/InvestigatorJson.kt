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
import com.copperleaf.arkham.models.api.Investigator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Paths

object InvestigatorJson {
    public val tags = listOf("FetchExpansionData", "output", "investigator")

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        localExpansionFiles: List<InputPathNode>,
        expansionCode: String,
        investigatorId: String,
        packsHttpNode: InputHttpNode,
        packHttpNodes: List<InputHttpNode>,
    ): TerminalPathNode = with(scope) {
        val investigatorNode = TerminalPathNode(
            tags = tags,
            baseOutputDir = graph.config.outputDir,
            outputPath = Paths.get("api/investigators/$investigatorId.json"),
            doRender = { nodes, os ->
                val localExpansions = LocalExpansionFile.getBodiesForOutput(scope, nodes).map { it.second }
                val localExpansion = LocalExpansionFile.getBodyForOutput(scope, nodes, expansionCode)
                val packsApi = ArkhamDbPacksApi.getBodyForOutput(scope, nodes)
                prettyJson.encodeToStream(
                    Investigator.serializer(),
                    createJson(
                        investigatorId,
                        localExpansions,
                        localExpansion,
                        packsApi,
                    ),
                    os,
                )
            },
        )
        addNode(investigatorNode)
        localExpansionFiles.forEach { addEdge(it, investigatorNode) }
        addEdge(packsHttpNode, investigatorNode)

        return investigatorNode
    }

    private fun createJson(
        investigatorId: String,
        localExpansions: List<LocalArkhamHorrorExpansion>,
        localExpansion: LocalArkhamHorrorExpansion,
        packsApi: List<ArkhamDbPack>,
    ): Investigator {
        return localExpansion
            .investigators
            .single { it.id == investigatorId }
            .asFullOutput(localExpansion.code, localExpansions)
    }
}