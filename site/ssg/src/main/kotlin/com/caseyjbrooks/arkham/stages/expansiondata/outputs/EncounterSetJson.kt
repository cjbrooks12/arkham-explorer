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
import com.copperleaf.arkham.models.api.EncounterSet
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Paths

object EncounterSetJson {
    public val tags = listOf("FetchExpansionData", "output", "encounter set")

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        localExpansionFiles: List<InputPathNode>,
        expansionSlug: String,
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
                val localExpansion = LocalExpansionFile.getBodyForOutput(scope, nodes, expansionSlug)
                val packsApi = ArkhamDbPacksApi.getBodyForOutput(scope, nodes)
                prettyJson.encodeToStream(
                    EncounterSet.serializer(),
                    createJson(
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
        encounterSetId: String,
        localExpansions: List<LocalArkhamHorrorExpansion>,
        localExpansion: LocalArkhamHorrorExpansion,
        packsApi: List<ArkhamDbPack>,
    ): EncounterSet {
        return localExpansion
            .encounterSets
            .single { it.id == encounterSetId }
            .asFullOutput(localExpansions)
    }
}
