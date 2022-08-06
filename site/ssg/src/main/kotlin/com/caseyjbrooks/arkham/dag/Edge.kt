package com.caseyjbrooks.arkham.dag

/**
 * A directed edge between two nodes. Once created, it cannot be changed. Both Nodes will keep a reference to this Edge
 * in their [Node.Meta], and the graph itself will also keep a list of all nodes and edges.
 */
data class Edge(
    /**
     * The starting node, which points toward [end].
     */
    val start: Node,

    /**
     * The ending node. [start] points to this node, not the other direction. For a 2-way connection, you must set up 2
     * different edges (though that would necessarily break the acyclic nature of the graph).
     */
    val end: Node,

    /**
     * An optional weight for this edge.
     */
    val weight: Double = 1.0,
) {
    /**
     * The iteration this node was created. Useful for querying nodes, and determining what to do next.
     *
     * DO NOT CHANGE THIS VALUE. This is managed internally by the graph.
     */
    var iteration: Int = -1

    override fun toString(): String {
        return "$start -> $end ($weight)"
    }
}
