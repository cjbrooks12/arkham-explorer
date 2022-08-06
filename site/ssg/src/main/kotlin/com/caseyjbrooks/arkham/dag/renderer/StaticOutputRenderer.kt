package com.caseyjbrooks.arkham.dag.renderer

import com.caseyjbrooks.arkham.dag.DependencyGraph
import com.caseyjbrooks.arkham.dag.Node
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Render the dirty outputs to the output directory immediately when requested. When a build process has completed, the
 * site in the output directory should be fully up-to-date.
 */
class StaticOutputRenderer : Renderer {
    override suspend fun start(graph: DependencyGraph) {
        // don't do anything here
    }

    override suspend fun renderDirtyOutputs(
        graph: DependencyGraph,
        dirtyOutputs: List<Node.Output>,
    ): Unit = coroutineScope {
        // pre-create all output files in series, to avoid concurrency issues when rendering them
        dirtyOutputs.forEach {
            it.prepareOutput(graph)
        }

        // render each output in parallel for highest performance
        dirtyOutputs
            .map { outputNode ->
                async {
                    outputNode.renderOutput(graph)
                    outputNode.rendered = true
                }
            }
            .awaitAll()
    }
}
