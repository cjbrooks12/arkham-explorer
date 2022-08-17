package com.caseyjbrooks.arkham.dag

import java.io.OutputStream

/**
 * A Node within the Dependency graph represents discrete documents or sources of data, and can be both Inputs and
 * Outputs to the graph. Specifically, all Nodes which only have edges ending at themselves would be considered pure
 * Outputs, while all Nodes which only have edges starting with themselves are pure Inputs. All nodes that are not pure
 * Inputs or Outputs are Intermediates, which may be an Input to another Node, and also an Output to the entire Graph.
 *
 * Any given Node is considered to depend on _all_ nodes that point to it, both directly or indirectly. More
 * specifically, if any of a Node's direct dependencies are dirty, then the node itself is dirty. Thus, if a dependency
 * of a dependency is dirty, then the node itself will eventually be marked dirty, and so.
 *
 * The goal of the graph is to iteratively build up the model for a website based on a combination of local files and
 * HTTP requests. Those documents can be combined in any way needed to produce the final website. The goal is to avoid
 * executing HTTP requests and writing files, so by explicitly modeling all dependencies, we can use graph algorithms
 * to check if any node may need to be re-rendered, and skip it otherwise.
 */
sealed interface Node {

    /**
     * Metadata for the node. This contains a combination of data that is supplied when created, and also managed by the
     * Graph.
     */
    data class Meta(
        /**
         * A unique name for this node. Must be fully unique among all nodes in the graph,
         */
        val name: String,

        /**
         * A list of tags helping to identify this node. The Graph itself ignores these nodes, they're just used for
         * querying nodes.
         */
        val tags: List<String> = emptyList(),
    ) {
        /**
         * The iteration this node was created. Useful for querying nodes, and determining what to do next.
         *
         * DO NOT CHANGE THIS VALUE. This is managed internally by the graph.
         */
        var iteration: Int = -1

        /**
         * A list of edges defining the relationship between two nodes. Anytime an Edge is added to the graph, it will
         * be shared by both nodes, and also stored directly within the graph itself.
         *
         * DO NOT CHANGE THIS VALUE. This is managed internally by the graph.
         */
        val edges: MutableList<Edge> = mutableListOf()
    }

    val meta: Node.Meta

    interface Input : Node {
        suspend fun preload(graph: DependencyGraph)
        suspend fun dirty(graph: DependencyGraph): Boolean
        suspend fun markClean(graph: DependencyGraph)
    }

    interface Output : Node {
        var rendered: Boolean

        fun exists(graph: DependencyGraph): Boolean
        suspend fun prepareOutput(graph: DependencyGraph)
        suspend fun renderOutput(graph: DependencyGraph)
        suspend fun renderOutput(graph: DependencyGraph, outputStream: OutputStream)
    }
}

