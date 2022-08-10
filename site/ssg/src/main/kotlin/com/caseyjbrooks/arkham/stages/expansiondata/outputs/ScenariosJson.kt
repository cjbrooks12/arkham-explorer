package com.caseyjbrooks.arkham.stages.expansiondata.outputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import java.nio.file.Paths
import kotlin.io.path.div

object ScenariosJson {
    public val tags = listOf("FetchExpansionData", "output", "scenarios")

    public suspend fun createOutputFile(
        scope: DependencyGraphBuilder.Scope,
        packsConfigNode: StartPathNode,
        packsHttpNode: StartHttpNode,
    ): TerminalPathNode = with(scope) {
        val node = TerminalPathNode(
            tags = tags,
            baseOutputDir = graph.config.outputDir / "api",
            outputPath = Paths.get("scenarios.json"),
            doRender = { nodes, os ->
                os.write("{}".toByteArray())
            },
        )
        addNode(node)
        addEdge(packsConfigNode, node)
        addEdge(packsHttpNode, node)

        return node
    }
}
