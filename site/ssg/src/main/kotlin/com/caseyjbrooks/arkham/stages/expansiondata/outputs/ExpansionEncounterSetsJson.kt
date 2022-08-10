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

object ExpansionEncounterSetsJson {
    public val tags = listOf("FetchExpansionData", "output", "expansion", "encounter sets")

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        localExpansionFiles: List<InputPathNode>,
        expansionSlug: String,
        packsHttpNode: InputHttpNode,
        packHttpNodes: List<InputHttpNode>,
    ): TerminalPathNode = with(scope) {
        val expansionEncounterSetsNode = TerminalPathNode(
            baseOutputDir = graph.config.outputDir,
            outputPath = Paths.get("api/expansions/$expansionSlug/encounter-sets.json"),
            doRender = { nodes, os ->
                val localExpansions = LocalExpansionFile.getBodiesForOutput(scope, nodes).map { it.second }
                val localExpansion = LocalExpansionFile.getBodyForOutput(scope, nodes, expansionSlug)
                val packsApi = ArkhamDbPacksApi.getBodyForOutput(scope, nodes)
                prettyJson.encodeToStream(
                    EncounterSetList.serializer(),
                    createJson(
                        localExpansions,
                        localExpansion,
                        packsApi,
                    ),
                    os,
                )
            },
            tags = ExpansionJson.tags + expansionSlug
        )
        addNode(expansionEncounterSetsNode)
        localExpansionFiles.forEach { addEdge(it, expansionEncounterSetsNode) }
        addEdge(packsHttpNode, expansionEncounterSetsNode)

        return expansionEncounterSetsNode
    }

    private fun createJson(
        localExpansions: List<LocalArkhamHorrorExpansion>,
        localExpansion: LocalArkhamHorrorExpansion,
        packsApi: List<ArkhamDbPack>,
    ): EncounterSetList {
        return EncounterSetList(
            encounterSets = localExpansion
                .encounterSets
                .map { it.asFullOutput(localExpansions) }
                .sortedBy { it.id }
        )
    }
}
