package com.caseyjbrooks.arkham.stages.expansiondata.inputs

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.Node
import com.caseyjbrooks.arkham.dag.http.prettyJson
import com.caseyjbrooks.arkham.dag.path.InputPathNode
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.stages.expansiondata.inputs.models.LocalArkhamHorrorExpansion
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.io.path.nameWithoutExtension

object LocalExpansionFile {
    public val tags = listOf("FetchExpansionData", "input", "expansion", "local")

    public suspend fun loadExpansionFile(
        scope: DependencyGraphBuilder.Scope,
        siteConfigNode: SiteConfigNode,
        localExpansionFile: Path,
    ): InputPathNode = with(scope) {
        val expansionSlug = localExpansionFile.nameWithoutExtension
        return addNodeAndEdge(
            start = siteConfigNode,
            newEndNode = StartPathNode(
                baseInputDir = graph.config.expansionsDir,
                inputPath = localExpansionFile,
                tags = tags + expansionSlug,
            )
        )
    }

// Get single nodes by name
// ---------------------------------------------------------------------------------------------------------------------

    public suspend fun getNode(
        scope: DependencyGraphBuilder.Scope,
        expansionSlug: String,
    ): InputPathNode = with(scope) {
        return graph.getNodeOfType<StartPathNode> { it.meta.tags == (tags + expansionSlug) }
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
        expansionSlug: String,
    ): LocalArkhamHorrorExpansion {
        return nodes
            .filterIsInstance<StartPathNode>()
            .single { it.meta.tags == (tags + expansionSlug) }
            .let { getBody(it) }
    }

    public suspend fun getBody(
        node: InputPathNode,
    ): LocalArkhamHorrorExpansion {
        return node.realInputFile().inputStream().use {
            prettyJson.decodeFromStream(LocalArkhamHorrorExpansion.serializer(), it)
        }
    }

    public suspend fun getCachedBody(
        scope: DependencyGraphBuilder.Scope,
        expansionSlug: String,
    ): Pair<InputPathNode, LocalArkhamHorrorExpansion> {
        val node = getNode(scope, expansionSlug)
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
