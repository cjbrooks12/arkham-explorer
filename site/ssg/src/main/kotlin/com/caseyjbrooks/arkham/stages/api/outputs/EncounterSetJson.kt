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
import com.copperleaf.arkham.models.api.EncounterSetDetails
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Paths

@Suppress("UNUSED_PARAMETER")
object EncounterSetJson {
    public val tags = listOf("FetchExpansionData", "output", "encounter set")

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        localExpansionFiles: List<InputPathNode>,
        expansionCode: String,
        encounterSetId: String,
        packsHttpNode: InputHttpNode,
        packHttpNodes: List<InputHttpNode>,
    ): TerminalPathNode = with(scope) {
        val encounterSetNode = TerminalPathNode(
            tags = tags,
            baseOutputDir = graph.config.outputDir,
            outputPath = Paths.get("api/encounter-sets/$encounterSetId.json"),
            doRender = { nodes, os ->
                val localExpansions = LocalExpansionFile.getBodiesForOutput(scope, nodes).map { it.second }
                val localExpansion = LocalExpansionFile.getBodyForOutput(scope, nodes, expansionCode)
                val packsApi = ArkhamDbPacksApi.getBodyForOutput(scope, nodes)
                prettyJson.encodeToStream(
                    EncounterSetDetails.serializer(),
                    createJson(
                        expansionCode,
                        encounterSetId,
                        localExpansions,
                        localExpansion,
                        packsApi,
                    ),
                    os,
                )
            },
        )
        addNode(encounterSetNode)
        localExpansionFiles.forEach { addEdge(it, encounterSetNode) }
        addEdge(packsHttpNode, encounterSetNode)

        return encounterSetNode
    }

    private fun createJson(
        expansionCode: String,
        encounterSetId: String,
        localExpansions: List<LocalArkhamHorrorExpansion>,
        localExpansion: LocalArkhamHorrorExpansion,
        packsApi: List<ArkhamDbPack>,
    ): EncounterSetDetails {
        return localExpansion
            .encounterSets
            .single { it.id == encounterSetId }
            .asFullOutput(expansionCode, localExpansions, packsApi)
    }
}
