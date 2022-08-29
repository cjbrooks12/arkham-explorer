package com.caseyjbrooks.arkham.stages.rss

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.prettyJson
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json

class RssFeedAggregator(
    private vararg val sources: RssFeedSource
) : DependencyGraphBuilder {
    private val http = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(prettyJson)
        }
        Logging { this.logger = Logger.SIMPLE }
    }

    private var startIteration = Int.MIN_VALUE
    override suspend fun DependencyGraphBuilder.Scope.buildGraph() {
        if (graph.containsNode { it is SiteConfigNode } && startIteration == Int.MIN_VALUE) {
            startIteration = graph.currentIteration
        }

        when (graph.currentIteration) {
            startIteration -> loadInputs()
            startIteration + 1 -> createOutputFiles()
        }
    }

    private suspend fun DependencyGraphBuilder.Scope.loadInputs() {
        val siteConfigNode = graph.getNodeOfType<SiteConfigNode>()

        sources
            .forEach { feedSource ->
                val feedResponse = feedSource.loadFeed(http)
                addNodeAndEdge(siteConfigNode, feedResponse)
            }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {

    }
}
