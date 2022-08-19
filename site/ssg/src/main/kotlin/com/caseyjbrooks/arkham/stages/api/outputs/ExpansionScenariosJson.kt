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
import com.caseyjbrooks.arkham.stages.api.outputs.utils.asLiteOutput
import com.copperleaf.arkham.models.api.ScenarioList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Paths

@Suppress("UNUSED_PARAMETER")
object ExpansionScenariosJson {
    public val tags = listOf("FetchExpansionData", "output", "expansion", "scenarios")

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        localExpansionFiles: List<InputPathNode>,
        expansionCode: String,
        packsHttpNode: InputHttpNode,
        packHttpNodes: List<InputHttpNode>,
    ): TerminalPathNode = with(scope) {
        val expansionScenariosNode = TerminalPathNode(
            baseOutputDir = graph.config.outputDir,
            outputPath = Paths.get("api/expansions/$expansionCode/scenarios.json"),
            doRender = { nodes, os ->
                val localExpansions = LocalExpansionFile.getBodiesForOutput(scope, nodes).map { it.second }
                val localExpansion = LocalExpansionFile.getBodyForOutput(scope, nodes, expansionCode)
                val packsApi = ArkhamDbPacksApi.getBodyForOutput(scope, nodes)
                prettyJson.encodeToStream(
                    ScenarioList.serializer(),
                    createJson(
                        localExpansions,
                        localExpansion,
                        packsApi,
                    ),
                    os,
                )
            },
            tags = ExpansionJson.tags + expansionCode
        )
        addNode(expansionScenariosNode)
        localExpansionFiles.forEach { addEdge(it, expansionScenariosNode) }
        addEdge(packsHttpNode, expansionScenariosNode)

        return expansionScenariosNode
    }

    private fun createJson(
        localExpansions: List<LocalArkhamHorrorExpansion>,
        localExpansion: LocalArkhamHorrorExpansion,
        packsApi: List<ArkhamDbPack>,
    ): ScenarioList {
        return ScenarioList(
            scenarios = localExpansion
                .scenarios
                .map { it.asLiteOutput(localExpansion.code, localExpansions, packsApi) }
                .sortedBy { it.id }
        )
    }
}
