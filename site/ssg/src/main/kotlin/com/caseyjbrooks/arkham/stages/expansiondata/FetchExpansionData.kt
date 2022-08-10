package com.caseyjbrooks.arkham.stages.expansiondata

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.prettyJson
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.stages.expansiondata.inputs.ArkhamDbPackCardsApi
import com.caseyjbrooks.arkham.stages.expansiondata.inputs.ArkhamDbPacksApi
import com.caseyjbrooks.arkham.stages.expansiondata.inputs.LocalExpansionFile
import com.caseyjbrooks.arkham.stages.expansiondata.inputs.LocalExpansionsFile
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.EncounterSetsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ExpansionEncounterSetsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ExpansionInvestigatorsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ExpansionJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ExpansionProductsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ExpansionScenariosJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ExpansionsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.InvestigatorsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ProductsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ScenariosJson
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
import kotlin.io.path.inputStream

@OptIn(ExperimentalSerializationApi::class)
class FetchExpansionData : DependencyGraphBuilder {
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

        val localExpansionsFile = LocalExpansionsFile.loadExpansionsFile(this, siteConfigNode)
        val packsApi = ArkhamDbPacksApi.loadPacksList(this, siteConfigNode, http)

        val expansions = prettyJson.decodeFromStream(
            ArkhamHorrorExpansionsIndex.serializer(),
            localExpansionsFile.realInputFile().inputStream()
        )

        expansions.expansions.forEach { packConfig ->
            LocalExpansionFile.loadExpansionFile(this, localExpansionsFile, packsApi, packConfig)
            packConfig.productCodes.forEach { productCode ->
                ArkhamDbPackCardsApi.loadPacksCards(this, localExpansionsFile, packsApi, packConfig, productCode, http)
            }
        }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {
        val localExpansionsFile = LocalExpansionsFile.getNode(this)
        val packsHttpNode = ArkhamDbPacksApi.getNode(this)

        val expansions = prettyJson.decodeFromStream(
            ArkhamHorrorExpansionsIndex.serializer(),
            localExpansionsFile.realInputFile().inputStream()
        )

        ExpansionsJson.createOutputFile(this, localExpansionsFile, packsHttpNode)
        ScenariosJson.createOutputFile(this, localExpansionsFile, packsHttpNode)
        EncounterSetsJson.createOutputFile(this, localExpansionsFile, packsHttpNode)
        InvestigatorsJson.createOutputFile(this, localExpansionsFile, packsHttpNode)
        ProductsJson.createOutputFile(this, localExpansionsFile, packsHttpNode)

        expansions.expansions.forEach { packConfig ->
            val localExpansionFile = LocalExpansionFile.getNode(this, packConfig)
            ExpansionJson.createOutputFile(this, localExpansionsFile, packsHttpNode, localExpansionFile, packConfig)
            ExpansionScenariosJson.createOutputFile(this, localExpansionsFile, packsHttpNode, localExpansionFile, packConfig)
            ExpansionEncounterSetsJson.createOutputFile(this, localExpansionsFile, packsHttpNode, localExpansionFile, packConfig)
            ExpansionInvestigatorsJson.createOutputFile(this, localExpansionsFile, packsHttpNode, localExpansionFile, packConfig)
            ExpansionProductsJson.createOutputFile(this, localExpansionsFile, packsHttpNode, localExpansionFile, packConfig)
        }
    }
}
