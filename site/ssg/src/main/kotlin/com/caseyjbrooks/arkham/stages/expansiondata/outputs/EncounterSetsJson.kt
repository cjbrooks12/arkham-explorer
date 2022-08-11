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
import com.copperleaf.arkham.models.api.EncounterSetList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Paths

object EncounterSetsJson {
    public val tags = listOf("FetchExpansionData", "output", "encounter sets")

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        local: List<InputPathNode>,
        packsHttpNode: InputHttpNode,
    ): TerminalPathNode = with(scope) {
        val encounterSetsOutputNode = TerminalPathNode(
            tags = tags,
            baseOutputDir = graph.config.outputDir,
            outputPath = Paths.get("api/encounter-sets.json"),
            doRender = { nodes, os ->
                val localExpansionFiles = LocalExpansionFile.getBodiesForOutput(scope, nodes).map { it.second }
                val packsApi = ArkhamDbPacksApi.getBodyForOutput(scope, nodes)
                prettyJson.encodeToStream(
                    EncounterSetList.serializer(),
                    createJson(localExpansionFiles, packsApi),
                    os,
                )
            },
        )
        addNode(encounterSetsOutputNode)
        local.forEach { localExpansionNode ->
            addEdge(localExpansionNode, encounterSetsOutputNode)
        }
        addEdge(packsHttpNode, encounterSetsOutputNode)

        return encounterSetsOutputNode
    }

    private fun createJson(
        localExpansionFiles: List<LocalArkhamHorrorExpansion>,
        packsApi: List<ArkhamDbPack>,
    ): EncounterSetList {
        return EncounterSetList(
            encounterSets = localExpansionFiles
                .flatMap { expansion ->
                    expansion
                        .encounterSets
                        .map { it.asFullOutput(expansion.code, localExpansionFiles) }
                        .sortedBy { it.id }
                }
        )
    }
}
