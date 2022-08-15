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
import com.copperleaf.arkham.models.api.Scenario
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Paths

object ScenarioJson {
    public val tags = listOf("FetchExpansionData", "output", "scenario")

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        localExpansionFiles: List<InputPathNode>,
        expansionCode: String,
        scenarioId: String,
        packsHttpNode: InputHttpNode,
        packHttpNodes: List<InputHttpNode>,
    ): TerminalPathNode = with(scope) {
        val scenarioNode = TerminalPathNode(
            tags = tags,
            baseOutputDir = graph.config.outputDir,
            outputPath = Paths.get("api/scenarios/$scenarioId.json"),
            doRender = { nodes, os ->
                val localExpansions = LocalExpansionFile.getBodiesForOutput(scope, nodes).map { it.second }
                val localExpansion = LocalExpansionFile.getBodyForOutput(scope, nodes, expansionCode)
                val packsApi = ArkhamDbPacksApi.getBodyForOutput(scope, nodes)
                prettyJson.encodeToStream(
                    Scenario.serializer(),
                    createJson(
                        expansionCode,
                        scenarioId,
                        localExpansions,
                        localExpansion,
                        packsApi,
                    ),
                    os,
                )
            },
        )
        addNode(scenarioNode)
        localExpansionFiles.forEach { addEdge(it, scenarioNode) }
        addEdge(packsHttpNode, scenarioNode)

        return scenarioNode
    }

    private fun createJson(
        expansionCode: String,
        scenarioId: String,
        localExpansions: List<LocalArkhamHorrorExpansion>,
        localExpansion: LocalArkhamHorrorExpansion,
        packsApi: List<ArkhamDbPack>,
    ): Scenario {
        return localExpansion
            .scenarios
            .single { it.id == scenarioId }
            .asFullOutput(localExpansion.code, localExpansions, packsApi)
    }
}
