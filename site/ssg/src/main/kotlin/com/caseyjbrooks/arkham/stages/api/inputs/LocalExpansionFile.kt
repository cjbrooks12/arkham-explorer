package com.caseyjbrooks.arkham.stages.api.inputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.Node
import com.caseyjbrooks.arkham.dag.http.prettyJson
import com.caseyjbrooks.arkham.dag.path.InputPathNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.io.path.nameWithoutExtension

@Suppress("UNUSED_PARAMETER")
object LocalExpansionFile {
    public val tags = listOf("FetchExpansionData", "input", "expansion", "local")

    public suspend fun loadExpansionFile(
        scope: DependencyGraphBuilder.Scope,
        siteConfigNode: SiteConfigNode,
        localExpansionFile: Path,
    ): InputPathNode = with(scope) {
        val expansionCode = localExpansionFile.nameWithoutExtension
        return addNodeAndEdge(
            start = siteConfigNode,
            newEndNode = StartPathNode(
                baseInputDir = graph.config.expansionsDir,
                inputPath = localExpansionFile,
                tags = tags + expansionCode,
            )
        )
    }

// Get single nodes by name
// ---------------------------------------------------------------------------------------------------------------------

    public suspend fun getNode(
        scope: DependencyGraphBuilder.Scope,
        expansionCode: String,
    ): InputPathNode = with(scope) {
        return graph.getNodeOfType<StartPathNode> { it.meta.tags == (tags + expansionCode) }
    }

    public suspend fun getBodiesForOutput(
        scope: DependencyGraphBuilder.Scope,
        nodes: List<Node>,
    ): List<Pair<InputPathNode, LocalArkhamHorrorExpansion>> {
        return nodes
            .filterIsInstance<StartPathNode>()
            .filter { it.meta.tags.containsAll(tags) }
            .map { it to getBody(it) }
    }

    public suspend fun getBodyForOutput(
        scope: DependencyGraphBuilder.Scope,
        nodes: List<Node>,
        expansionCode: String,
    ): LocalArkhamHorrorExpansion {
        return nodes
            .filterIsInstance<StartPathNode>()
            .single { it.meta.tags == (tags + expansionCode) }
            .let { getBody(it) }
    }

    @OptIn(ExperimentalSerializationApi::class)
    public suspend fun getBody(
        node: InputPathNode,
    ): LocalArkhamHorrorExpansion {
        return node.realInputFile().inputStream().use {
            prettyJson
                .decodeFromStream(LocalArkhamHorrorExpansion.serializer(), it)
                .copy(code = node.inputPath.nameWithoutExtension)
        }
    }

    public suspend fun getCachedBody(
        scope: DependencyGraphBuilder.Scope,
        expansionCode: String,
    ): Pair<InputPathNode, LocalArkhamHorrorExpansion> {
        val node = getNode(scope, expansionCode)
        return node to getBody(node)
    }

// Get all nodes of this type
// ---------------------------------------------------------------------------------------------------------------------

    public suspend fun getNodes(
        scope: DependencyGraphBuilder.Scope,
    ): List<InputPathNode> = with(scope) {
        return graph.getNodesOfType<StartPathNode> { it.meta.tags.containsAll(tags) }
    }
}
