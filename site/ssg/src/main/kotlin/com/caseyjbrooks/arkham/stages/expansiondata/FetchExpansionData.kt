package com.caseyjbrooks.arkham.stages.expansiondata

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.dag.http.prettyJson
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.stages.expansiondata.models.ArkhamDbPackSummary
import com.caseyjbrooks.arkham.stages.expansiondata.models.ArkhamDbProductSummary
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.builtins.ListSerializer
import java.nio.file.Paths
import kotlin.io.path.div

class FetchExpansionData : DependencyGraphBuilder {
    companion object {
        val packs: String = "https://arkhamdb.com/api/public/packs"
        fun cards(packName: String): String = "https://arkhamdb.com/api/public/cards/${packName}.json"
    }

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
            startIteration -> loadPacksIndex()
            startIteration + 1 -> loadEachPack()
            startIteration + 2 -> createOutputFiles()
        }
    }

    private suspend fun DependencyGraphBuilder.Scope.loadPacksIndex() {
        val siteConfigNode = graph.getNodeOfType<SiteConfigNode>()
        addNodeAndEdge(
            start = siteConfigNode,
            newEndNode = StartHttpNode(
                httpClient = http,
                url = packs,
                tags = listOf("FetchExpansionData", "packs"),
            )
        )
    }

    private suspend fun DependencyGraphBuilder.Scope.getPackSummary(): Pair<StartHttpNode, List<ArkhamDbPackSummary>> {
        val packsNode = graph.getNodeOfType<StartHttpNode> { it.meta.tags == listOf("FetchExpansionData", "packs") }

        val packs: List<ArkhamDbProductSummary> = packsNode.getResponse(
            graph,
            ListSerializer(ArkhamDbProductSummary.serializer()),
        )
        val groupedPacks = packs.groupBy { it.cyclePosition }

        return packsNode to listOf(
            ArkhamDbPackSummary(1, "coreSet", groupedPacks.getValue(1)),
            ArkhamDbPackSummary(2, "theDunwichLegacy", groupedPacks.getValue(2)),
            ArkhamDbPackSummary(3, "thePathToCarcosa", groupedPacks.getValue(3)),
            ArkhamDbPackSummary(4, "theForgottenAge", groupedPacks.getValue(4)),
            ArkhamDbPackSummary(5, "theCircleUndone", groupedPacks.getValue(5)),
            ArkhamDbPackSummary(6, "theDreamEaters", groupedPacks.getValue(6)),
            ArkhamDbPackSummary(7, "theTheInnsmouthConspiracy", groupedPacks.getValue(7)),
            ArkhamDbPackSummary(8, "edgeOfTheEarth", groupedPacks.getValue(8)),
            ArkhamDbPackSummary(9, "theScarletKeys", groupedPacks.getValue(9)),
            ArkhamDbPackSummary(50, "returnTos", groupedPacks.getValue(50)),
            ArkhamDbPackSummary(60, "standaloneInvestigators", groupedPacks.getValue(60)),
            ArkhamDbPackSummary(70, "standaloneScenarios", groupedPacks.getValue(70)),
            ArkhamDbPackSummary(80, "books", groupedPacks.getValue(80)),
            ArkhamDbPackSummary(90, "challengeScenarios", groupedPacks.getValue(90)),
        )
    }

    private suspend fun DependencyGraphBuilder.Scope.loadEachPack() {
        val (packsNode, packSummaries) = getPackSummary()

        packSummaries.forEach { packSummary ->
            packSummary
                .products
                .forEach { productSummary ->
                    addNodeAndEdge(
                        start = packsNode,
                        newEndNode = StartHttpNode(
                            httpClient = http,
                            url = Companion.cards(productSummary.code),
                            tags = listOf(
                                "FetchExpansionData",
                                "pack cards",
                                packSummary.name,
                                productSummary.code
                            ),
                        )
                    )
                }
        }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {
        val (packsNode, packSummaries) = getPackSummary()

        packSummaries.forEach { packSummary ->
            val outputPath = Paths.get("expansions/${packSummary.name}.json")
            val packNodes = graph.getNodesOfType<StartHttpNode> {
                it.meta.tags.containsAll(
                    listOf(
                        "FetchExpansionData",
                        "pack cards",
                        packSummary.name,
                    )
                )
            }

            val outputNode = TerminalPathNode(
                tags = listOf("FetchExpansionData", "output", packSummary.name),
                baseOutputDir = graph.config.outputDir / "api",
                outputPath = outputPath,
                doRender = { nodes, os ->
                    os.write("{}".toByteArray())
                }
            )

            addNode(outputNode)

            addEdge(packsNode, outputNode)
            packNodes.forEach { packNode ->
                addEdge(packNode, outputNode)
            }
        }
    }
}
