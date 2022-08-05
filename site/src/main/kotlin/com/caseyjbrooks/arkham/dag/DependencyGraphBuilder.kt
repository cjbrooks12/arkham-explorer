package com.caseyjbrooks.arkham.dag

interface DependencyGraphBuilder {
    suspend fun DependencyGraphBuilder.Scope.buildGraph()

    class Scope(val graph: DependencyGraph) {
        internal val _temporaryNodes: MutableList<Node> = mutableListOf()
        internal val _temporaryEdges: MutableList<Edge> = mutableListOf()

        suspend fun addNode(node: Node) {
            addNodeInternal(node)
        }

        private suspend fun addNodeInternal(node: Node) {
            _temporaryNodes.add(node)
        }

        suspend fun addEdge(start: Node, end: Node, weight: Double = 1.0) {
            addEdgeInternal(start, end, weight)
        }

        suspend fun addEdgeInternal(start: Node, end: Node, weight: Double = 1.0) {
            val edge = Edge(
                start = start,
                end = end,
                weight = weight,
            )
            _temporaryEdges.add(edge)
        }

        suspend fun addNodeAndEdge(start: Node, newEndNode: Node, weight: Double = 1.0) {
            addNodeInternal(newEndNode)
            addEdgeInternal(start, newEndNode, weight)
        }

    }
}
