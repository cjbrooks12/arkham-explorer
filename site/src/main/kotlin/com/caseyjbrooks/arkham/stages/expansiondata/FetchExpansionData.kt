package com.caseyjbrooks.arkham.stages.expansiondata

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class FetchExpansionData : DependencyGraphBuilder {
    private val http = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
            })
        }
        install(HttpCache) {}
        defaultRequest {
            url("https://arkhamdb.com/api/")
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

    }

    private suspend fun DependencyGraphBuilder.Scope.loadEachPack() {

    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {

    }
}

//class FetchExpansionData(
//    private val cacheService: CacheService,
//    private val resourceService: ResourceService,
//) : ProcessingStage {
//    private val http = HttpClient(CIO) {
//        install(ContentNegotiation) {
//            json(Json {
//                isLenient = true
//                this.encodeDefaults
//            })
//        }
//        install(HttpCache) {}
//        defaultRequest {
//            url("https://arkhamdb.com/api/")
//        }
//        Logging { this.logger = Logger.SIMPLE }
//    }
//
//    override suspend fun process(): Iterable<Cacheable.Input<*, *>> = coroutineScope {
//        http.request()
//
//
//
//        val packs: List<ArkhamDbProductSummary> = http.get("public/packs/").body()
//        val groupedPacks = packs.groupBy { it.cyclePosition }
//
//        val products = listOf(
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
//
//        val productsWithCards: List<ArkhamDbPackDetails> = products
//            .map { packSummary ->
//                async {
//                    ArkhamDbPackDetails(
//                        cyclePosition = packSummary.cyclePosition,
//                        name = packSummary.name,
//                        products = packSummary
//                            .products
//                            .map { productSummary ->
//                                async {
//                                    ArkhamDbProductDetails(
//                                        summary = productSummary,
//                                        cards = http.get("public/cards/${productSummary.code}.json").body()
//                                    )
//                                }
//                            }
//                            .awaitAll()
//                    )
//                }
//            }
//            .awaitAll()
//
//        productsWithCards.map { packDetails ->
//            UncacheableInput(
//                rootDir = cacheService.rootDir,
//                data = packDetails,
//                outputs = {
//                    listOf(
//                        OutputFromUncacheableInput(
//                            outputDir = cacheService.outputDir,
//                            outputPath = Paths.get("expansions/${packDetails.name}.json"),
//                            renderContent = {
//                                Json.encodeToString(ArkhamDbPackDetails.serializer(), packDetails)
//                            }
//                        )
//                    )
//                }
//            )
//        }
//    }
//}
