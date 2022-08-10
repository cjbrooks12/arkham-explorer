package com.caseyjbrooks.arkham.stages.expansiondata.outputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.utils.destruct2
import java.nio.file.Paths
import kotlin.io.path.div
import kotlin.io.path.readBytes

object ExpansionsJson {
    public val tags = listOf("FetchExpansionData", "output", "expansions")

    /**
     * A local index of packs that weill ultimately serve to tell the SPA which packs are available
     */
    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        packsConfigNode: StartPathNode,
        packsHttpNode: StartHttpNode,
    ): TerminalPathNode = with(scope) {
        val node = TerminalPathNode(
            tags = tags,
            baseOutputDir = graph.config.outputDir / "api",
            outputPath = Paths.get("expansions.json"),
            doRender = { nodes, os ->
                // TODO: more intelligently map data from both the local file and API response to get more data in the
                //  rendered index file
                val (packsConfig, packsHttp) = nodes.destruct2<StartPathNode, StartHttpNode>()
                os.write(packsConfig.realInputFile().readBytes())
            },
        )
        addNode(node)
        addEdge(packsConfigNode, node)
        addEdge(packsHttpNode, node)

        return node
    }
}
