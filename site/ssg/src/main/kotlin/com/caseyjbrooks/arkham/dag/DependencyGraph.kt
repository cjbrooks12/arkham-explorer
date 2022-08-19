package com.caseyjbrooks.arkham.dag

import com.caseyjbrooks.arkham.dag.renderer.Renderer
import com.caseyjbrooks.arkham.dag.renderer.StaticOutputRenderer
import com.caseyjbrooks.arkham.dag.updater.ImmediateUpdaterService
import com.caseyjbrooks.arkham.dag.updater.UpdaterService
import com.caseyjbrooks.arkham.utils.ResourceService
import com.caseyjbrooks.arkham.utils.SiteConfiguration
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@Suppress("UNCHECKED_CAST")
class DependencyGraph(
    public val config: SiteConfiguration,
    /**
     * The builders which will be generating nodes and making connections within the graph
     */
    private vararg val builders: DependencyGraphBuilder,

    private val renderers: List<Renderer> = listOf(StaticOutputRenderer()),
    private val updater: UpdaterService = ImmediateUpdaterService(),
    public val resourceService: ResourceService = ResourceService(),

    /**
     * An upper-bound on the number of iterations to run, to help prevent accidental infinite loops.
     */
    private val maxIterations: Int = 10,
) {

    private val _nodes: MutableList<Node> = mutableListOf()
    public val nodes: List<Node> get() = _nodes.toList()

    private val _edges: MutableList<Edge> = mutableListOf()
    public val edges: List<Edge> get() = _edges.toList()

    private var _currentIteration = 0
    public val currentIteration get() = _currentIteration

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Initial)
    public val state: StateFlow<State> get() = _state.asStateFlow()

    private var outputsRendered = 0
    private var outputsSkipped = 0

    enum class State {
        Initial, Building, Ready
    }

// Build and execute graph
// ---------------------------------------------------------------------------------------------------------------------

    suspend fun executeGraph() = coroutineScope {
        withContext(Dispatchers.IO + CoroutineExceptionHandler { _, t -> t.printStackTrace() }) {
            renderers.forEach { renderer ->
                launch { with(renderer) { start(this@DependencyGraph) } }
            }

            launch {
                updater
                    .watchForChanges(this@DependencyGraph)
                    .conflate()
                    .onEach { buildSiteModel() }
                    .launchIn(this)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun buildSiteModel() {
        _state.value = State.Building
        val time = measureTime {
            println("Staring build")
            _nodes.clear()
            _edges.clear()
            _currentIteration = 0
            outputsRendered = 0
            outputsSkipped = 0

            val graph = this@DependencyGraph
            for (iteration in 1..maxIterations) {
                val hadChanges = coroutineScope {
                    // mark this iteration as clean
                    _currentIteration = iteration

                    // let the builders run their next step
                    val scopes = builders
                        .map { builder ->
                            async {
                                with(builder) {
                                    DependencyGraphBuilder.Scope(graph).apply {
                                        buildGraph()
                                    }
                                }
                            }
                        }
                        .awaitAll()

                    // save the changes that were temporarily made to this graph, so they become available for the
                    // next iteration
                    commitIteration(scopes)
                }

                if (!hadChanges) {
                    // if the builders did not make any changes to the graph, then we're done. Break out of the loop
                    break
                }
            }

            markAllNodesClean()
        }

        _state.value = State.Ready
        println("Site completed after $time")
        println("  - ${currentIteration - 1}/$maxIterations iterations")
        println("  - Skipped $outputsSkipped out of ${outputsRendered + outputsSkipped} outputs")
    }

    /**
     * Commit the temporary changes into the graph. Returns true if changes were made, false otherwise.
     */
    private suspend fun commitIteration(scopes: List<DependencyGraphBuilder.Scope>): Boolean {
        val _temporaryNodes = scopes.flatMap { it._temporaryNodes }
        val _temporaryEdges = scopes.flatMap { it._temporaryEdges }

        if (_temporaryNodes.isEmpty() && _temporaryEdges.isEmpty()) {
            return false
        }

        // cache references to all the outputs that were added this iteration
        val (newInputs, newOutputs) = _temporaryNodes.partition { it is Node.Input }

        // move all the temporary nodes into the list of actual nodes
        _temporaryNodes.forEach { tempNode ->
            check(_nodes.none { it.meta.name == tempNode.meta.name }) {
                "A node with name ${tempNode.meta.name} already exists in graph!"
            }
            tempNode.meta.iteration = currentIteration
            _nodes.add(tempNode)
        }

        // move all the temporary edges into the list of actual nodes, and connect each edge to its associated nodes
        _temporaryEdges.forEach { edge ->
            check(_nodes.singleOrNull { node -> node.meta.name == edge.start.meta.name } != null) {
                "A single node with name ${edge.start.meta.name} must in graph!"
            }
            check(_nodes.singleOrNull { node -> node.meta.name == edge.end.meta.name } != null) {
                "A single node with name ${edge.end.meta.name} must in graph!"
            }

            edge.start.meta.edges.add(edge)
            edge.end.meta.edges.add(edge)
            edge.iteration = currentIteration
        }
        _edges.addAll(_temporaryEdges)

        preloadInputs(newInputs as List<Node.Input>)

        // check if the new outputs are dirty, and render them if so
        renderDirtyOutputs(newOutputs as List<Node.Output>)

        return true
    }

    private suspend fun preloadInputs(
        newInputs: List<Node.Input>
    ) = coroutineScope {
        newInputs.map { inputNode ->
            async { inputNode.preload(this@DependencyGraph) }
        }.awaitAll()
    }

    private suspend fun renderDirtyOutputs(
        newOutputs: List<Node.Output>
    ) = coroutineScope {
        val (dirtyOutputs, cleanOutputs) = newOutputs.partition { it.isDirty() }

        outputsSkipped += cleanOutputs.size
        outputsRendered += dirtyOutputs.size

        renderers.forEach { renderer ->
            renderer.renderDirtyOutputs(this@DependencyGraph, dirtyOutputs)
        }
    }

    private suspend fun markAllNodesClean() {
        nodes.filterIsInstance<Node.Input>().forEach {
            it.markClean(this@DependencyGraph)
        }
    }

    private suspend fun Node.Output.isDirty(): Boolean {
        return if (!this.exists(this@DependencyGraph)) {
            true
        } else if (this.rendered) {
            false
        } else {
            val allDependencies = this.getDependencies()
            val inputDependencies = allDependencies.filterIsInstance<Node.Input>()
            val dirtyInputDependencies = inputDependencies.filter { it.dirty(this@DependencyGraph) }
            dirtyInputDependencies.isNotEmpty()
        }
    }

    private fun Node.getDependencies(): List<Node> {
        val edgesToThisNode = this.meta.edges.filter { it.end == this }
        val depsOfThisNode = edgesToThisNode.flatMap { edge -> setOf(edge.start) + edge.start.getDependencies() }
        return depsOfThisNode
    }

// Builder DSL
// ---------------------------------------------------------------------------------------------------------------------

    suspend fun containsNode(matcher: (Node) -> Boolean): Boolean {
        return _nodes.any(matcher)
    }

    suspend fun getNode(matcher: (Node) -> Boolean): Node {
        return _nodes.single(matcher)
    }

    suspend inline fun <reified T : Node> getNodeOfType(matcher: (T) -> Boolean = { true }): T {
        return nodes.filterIsInstance<T>().single(matcher)
    }

    suspend inline fun <reified T : Node> getNodesOfType(matcher: (T) -> Boolean = { true }): List<T> {
        return nodes
            .filterIsInstance<T>()
            .filter(matcher)
    }

}
