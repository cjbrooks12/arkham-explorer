package com.caseyjbrooks.arkham.dag

interface DependencyGraphBuilder {
    suspend fun DependencyGraphBuilder.Scope.buildGraph()

    class Scope(val graph: DependencyGraph) {
        internal val _temporaryNodes: MutableList<Node> = mutableListOf()
        internal val _temporaryEdges: MutableList<Edge> = mutableListOf()

        suspend fun <T : Node> addNode(node: T): T {
            _temporaryNodes.add(node)
            return node
        }

        suspend fun addEdge(start: Node, end: Node, weight: Double = 1.0) {
            val edge = Edge(
                start = start,
                end = end,
                weight = weight,
            )
            _temporaryEdges.add(edge)
        }

        suspend fun <T : Node> addNodeAndEdge(start: Node, newEndNode: T, weight: Double = 1.0): T {
            addNode(newEndNode)
            addEdge(start, newEndNode, weight)
            return newEndNode
        }

    }
}
