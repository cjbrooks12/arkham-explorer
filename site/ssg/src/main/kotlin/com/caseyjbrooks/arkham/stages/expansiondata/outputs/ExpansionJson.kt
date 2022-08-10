package com.caseyjbrooks.arkham.stages.expansiondata.outputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.site.BuildConfig
import com.caseyjbrooks.arkham.utils.destruct3
import com.copperleaf.arkham.models.ArkhamHorrorExpansionsIndex
import java.nio.file.Paths
import kotlin.io.path.div
import kotlin.io.path.readText

object ExpansionJson {
    public val tags = listOf("FetchExpansionData", "output", "expansion")

    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        packsConfigNode: StartPathNode,
        packsHttpNode: StartHttpNode,
        localExpansionFile: StartPathNode,
        packConfig: ArkhamHorrorExpansionsIndex.ArkhamHorrorExpansionIndex,
    ): TerminalPathNode = with(scope) {
        val node = TerminalPathNode(
            baseOutputDir = graph.config.outputDir / "api/expansions",
            outputPath = Paths.get("${packConfig.slug}.json"),
            doRender = { nodes, os ->
                val (config, http, expansionInput) = nodes.destruct3<StartPathNode, StartHttpNode, StartPathNode>()

                expansionInput
                    .realInputFile()
                    .readText()
                    .replace("{{baseUrl}}", BuildConfig.BASE_URL)
                    .let { os.write(it.toByteArray()) }
            },
            tags = tags + packConfig.slug
        )
        addNode(node)
        addEdge(packsConfigNode, node)
        addEdge(packsHttpNode, node)
        addEdge(localExpansionFile, node)

        return node
    }
}
