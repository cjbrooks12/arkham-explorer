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
import com.caseyjbrooks.arkham.stages.expansiondata.utils.asLiteOutput
import com.copperleaf.arkham.models.api.ExpansionList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Paths

object ExpansionsJson {
    public val tags = listOf("FetchExpansionData", "output", "expansions")

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        local: List<InputPathNode>,
        packsHttpNode: InputHttpNode,
    ): TerminalPathNode = with(scope) {
        val expansionsOutputNode = TerminalPathNode(
            tags = tags,
            baseOutputDir = graph.config.outputDir,
            outputPath = Paths.get("api/expansions.json"),
            doRender = { nodes, os ->
                val localExpansionFiles = LocalExpansionFile.getBodiesForOutput(scope, nodes)
                val packsApi = ArkhamDbPacksApi.getBodyForOutput(scope, nodes)
                prettyJson.encodeToStream(
                    ExpansionList.serializer(),
                    createJson(localExpansionFiles, packsApi),
                    os,
                )
            },
        )
        addNode(expansionsOutputNode)
        local.forEach { localExpansionNode ->
            addEdge(localExpansionNode, expansionsOutputNode)
        }
        addEdge(packsHttpNode, expansionsOutputNode)

        return expansionsOutputNode
    }

    private fun createJson(
        localExpansions: List<Pair<InputPathNode, LocalArkhamHorrorExpansion>>,
        packsApi: List<ArkhamDbPack>,
    ): ExpansionList {
        return ExpansionList(
            expansions = localExpansions
                .map { (node, localExpansion) ->
                    localExpansion.asLiteOutput(localExpansion.code, localExpansions.map { it.second })
                }
                .sortedBy { it.id }
        )
    }
}
