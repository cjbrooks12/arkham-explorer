package com.caseyjbrooks.arkham.stages.expansiondata

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.dag.http.prettyJson
import com.caseyjbrooks.arkham.dag.path.StartPathNode
import com.caseyjbrooks.arkham.dag.path.TerminalPathNode
import com.caseyjbrooks.arkham.site.BuildConfig
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.utils.destruct1
import com.caseyjbrooks.arkham.utils.destruct3
import com.copperleaf.arkham.models.ArkhamHorrorExpansionsIndex
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.Paths
import kotlin.io.path.div
import kotlin.io.path.inputStream
import kotlin.io.path.readBytes
import kotlin.io.path.readText

@OptIn(ExperimentalSerializationApi::class)
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

        // a local index of packs that weill ultimately serve to tell the SPA which packs are available
        addNodeAndEdge(
            start = siteConfigNode,
            newEndNode = StartPathNode(
                baseInputDir = graph.config.dataDir,
                inputPath = Paths.get("expansions.json"),
                tags = listOf("FetchExpansionData", "packs", "config"),
            )
        )

        // GET https://arkhamdb.com/api/public/packs, to mix additional information for each cycle/pack into those
        // manually configured.
        addNodeAndEdge(
            start = siteConfigNode,
            newEndNode = StartHttpNode(
                httpClient = http,
                url = packs,
                tags = listOf("FetchExpansionData", "packs", "http"),
            )
        )
    }

//    private suspend fun DependencyGraphBuilder.Scope.getPackSummary(): Triple<StartPathNode, StartHttpNode, List<ArkhamDbPackSummary>> {
//        val packsConfigNode = graph.getNodeOfType<StartPathNode> { it.meta.tags == listOf("FetchExpansionData", "packs", "config") }
//        val packsHttpNode = graph.getNodeOfType<StartHttpNode> { it.meta.tags == listOf("FetchExpansionData", "packs", "http") }
//
//        val packs: List<ArkhamDbProductSummary> = packsHttpNode.getResponse(
//            graph,
//            ListSerializer(ArkhamDbProductSummary.serializer()),
//        )
//        val groupedPacks = packs.groupBy { it.cyclePosition }
//
//        val expansionsIndex = prettyJson.decodeFromStream(
//            ArkhamHorrorExpansionsIndex.serializer(),
//            packsConfigNode.realInputFile().inputStream()
//        )
//
//        expansionsIndex.expansions
//
//        return packsNode to listOf(
//            ArkhamDbPackSummary(1, "coreSet", groupedPacks.getValue(1)),
//            ArkhamDbPackSummary(2, "theDunwichLegacy", groupedPacks.getValue(2)),
//            ArkhamDbPackSummary(3, "thePathToCarcosa", groupedPacks.getValue(3)),
//            ArkhamDbPackSummary(4, "theForgottenAge", groupedPacks.getValue(4)),
//            ArkhamDbPackSummary(5, "theCircleUndone", groupedPacks.getValue(5)),
//            ArkhamDbPackSummary(6, "theDreamEaters", groupedPacks.getValue(6)),
//            ArkhamDbPackSummary(7, "theTheInnsmouthConspiracy", groupedPacks.getValue(7)),
//            ArkhamDbPackSummary(8, "edgeOfTheEarth", groupedPacks.getValue(8)),
//            ArkhamDbPackSummary(9, "theScarletKeys", groupedPacks.getValue(9)),
//            ArkhamDbPackSummary(50, "returnTos", groupedPacks.getValue(50)),
//            ArkhamDbPackSummary(60, "standaloneInvestigators", groupedPacks.getValue(60)),
//            ArkhamDbPackSummary(70, "standaloneScenarios", groupedPacks.getValue(70)),
//            ArkhamDbPackSummary(80, "books", groupedPacks.getValue(80)),
//            ArkhamDbPackSummary(90, "challengeScenarios", groupedPacks.getValue(90)),
//        )
//    }

    private suspend fun DependencyGraphBuilder.Scope.loadEachPack() {
        val packsConfigNode = graph.getNodeOfType<StartPathNode> {
            it.meta.tags == listOf("FetchExpansionData", "packs", "config")
        }
        val packsHttpNode = graph.getNodeOfType<StartHttpNode> {
            it.meta.tags == listOf("FetchExpansionData", "packs", "http")
        }

        val expansionsIndex = prettyJson.decodeFromStream(
            ArkhamHorrorExpansionsIndex.serializer(),
            packsConfigNode.realInputFile().inputStream()
        )

        expansionsIndex.expansions.forEach { packConfig ->
            val expansionInputFileNode = StartPathNode(
                baseInputDir = graph.config.dataDir,
                inputPath = Paths.get("${packConfig.slug}.json"),
                tags = listOf("FetchExpansionData", "pack", "config", packConfig.slug),
            )
            addNode(expansionInputFileNode)
            addEdge(packsConfigNode, expansionInputFileNode)
            addEdge(packsHttpNode, expansionInputFileNode)
        }


//        val (packsNode, packSummaries) = getPackSummary()

//        packSummaries.forEach { packSummary ->
//            packSummary
//                .products
//                .forEach { productSummary ->
//                    addNodeAndEdge(
//                        start = packsNode,
//                        newEndNode = StartHttpNode(
//                            httpClient = http,
//                            url = Companion.cards(productSummary.code),
//                            tags = listOf(
//                                "FetchExpansionData",
//                                "pack cards",
//                                packSummary.name,
//                                productSummary.code
//                            ),
//                        )
//                    )
//                }
//        }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {
        val packsConfigNode = graph.getNodeOfType<StartPathNode> {
            it.meta.tags == listOf("FetchExpansionData", "packs", "config")
        }
        val packsHttpNode = graph.getNodeOfType<StartHttpNode> {
            it.meta.tags == listOf("FetchExpansionData", "packs", "http")
        }

        val expansionsIndex = prettyJson.decodeFromStream(
            ArkhamHorrorExpansionsIndex.serializer(),
            packsConfigNode.realInputFile().inputStream()
        )

        expansionsIndex.expansions.forEach { packConfig ->
            val expansionInputFileNode = graph.getNodeOfType<StartPathNode> {
                it.meta.tags == listOf("FetchExpansionData", "pack", "config", packConfig.slug)
            }
            val outputNode = TerminalPathNode(
                baseOutputDir = graph.config.outputDir / "api/expansions",
                outputPath = Paths.get("${packConfig.slug}.json"),
                doRender = { nodes, os ->
                    val (config, http, expansionInput) = nodes.destruct3<StartPathNode, StartHttpNode, StartPathNode>()

                    expansionInput
                        .realInputFile()
                        .readText()
                        .replace("{{baseUrl}}", BuildConfig.BASE_URL)
                        .let { os.write(it.toByteArray()) }
                },
                tags = listOf("FetchExpansionData", "output", "pack", packConfig.slug)
            )
            addNode(outputNode)
            addEdge(packsConfigNode, outputNode)
            addEdge(packsHttpNode, outputNode)
            addEdge(expansionInputFileNode, outputNode)
        }

        addNodeAndEdge(
            start = graph.getNodeOfType<StartPathNode> {
                it.meta.tags == listOf("FetchExpansionData", "packs", "config")
            },
            newEndNode = TerminalPathNode(
                tags = listOf("FetchExpansionData", "output", "packs", "config"),
                baseOutputDir = graph.config.outputDir / "api",
                outputPath = Paths.get("expansions.json"),
                doRender = { nodes, os ->
                    val (node) = nodes.destruct1<StartPathNode>()
                    os.write(node.realInputFile().readBytes())
                },
            )
        )
    }
}
