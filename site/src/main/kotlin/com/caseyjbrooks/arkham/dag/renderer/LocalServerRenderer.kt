package com.caseyjbrooks.arkham.dag.renderer

import com.caseyjbrooks.arkham.dag.DependencyGraph
import com.caseyjbrooks.arkham.dag.Node

/**
 * Start a local HTTP server and only render the output content when actually requested. This should not write any files
 * to the output directory.
 */
class LocalServerRenderer : Renderer {
    override suspend fun start(graph: DependencyGraph) {

    }

    override suspend fun renderDirtyOutputs(graph: DependencyGraph, dirtyOutputs: List<Node.Output>) {

    }
}
