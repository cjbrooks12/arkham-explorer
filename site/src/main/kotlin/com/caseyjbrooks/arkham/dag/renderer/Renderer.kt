package com.caseyjbrooks.arkham.dag.renderer

import com.caseyjbrooks.arkham.dag.DependencyGraph
import com.caseyjbrooks.arkham.dag.Node

/**
 * Renders the collected Inputs and Outputs.
 */
interface Renderer {
    suspend fun start(graph: DependencyGraph)
    suspend fun renderDirtyOutputs(graph: DependencyGraph, dirtyOutputs: List<Node.Output>)
}
