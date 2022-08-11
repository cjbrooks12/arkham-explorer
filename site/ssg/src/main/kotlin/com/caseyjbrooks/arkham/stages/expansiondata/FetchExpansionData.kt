package com.caseyjbrooks.arkham.stages.expansiondata

import com.caseyjbrooks.arkham.dag.DependencyGraphBuilder
import com.caseyjbrooks.arkham.dag.http.prettyJson
import com.caseyjbrooks.arkham.stages.config.SiteConfigNode
import com.caseyjbrooks.arkham.stages.expansiondata.inputs.ArkhamDbPackCardsApi
import com.caseyjbrooks.arkham.stages.expansiondata.inputs.ArkhamDbPacksApi
import com.caseyjbrooks.arkham.stages.expansiondata.inputs.LocalExpansionFile
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.EncounterSetJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.EncounterSetsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ExpansionEncounterSetsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ExpansionInvestigatorsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ExpansionJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ExpansionProductsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ExpansionScenariosJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ExpansionsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.InvestigatorJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.InvestigatorsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ProductsJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ScenarioJson
import com.caseyjbrooks.arkham.stages.expansiondata.outputs.ScenariosJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.io.path.nameWithoutExtension

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

        graph
            .resourceService
            .getFilesInDir(graph.config.expansionsDir)
            .forEach { expansionFile ->
                LocalExpansionFile.loadExpansionFile(this, siteConfigNode, expansionFile)
            }

        val packsApiNode = ArkhamDbPacksApi.loadPacksList(this, siteConfigNode, http)
        val packsBody = ArkhamDbPacksApi.getBody(this, packsApiNode)
        packsBody.forEach { pack ->
            ArkhamDbPackCardsApi.loadPacksCards(this, packsApiNode, pack.code, http)
        }
    }

    private suspend fun DependencyGraphBuilder.Scope.createOutputFiles() {
        val expansionNodes = LocalExpansionFile.getNodes(this)
        val packsHttpNode = ArkhamDbPacksApi.getNode(this)
        val packHttpNodes = ArkhamDbPackCardsApi.getNodes(this)

        ExpansionsJson.createOutputFile(this, expansionNodes, packsHttpNode)
        ScenariosJson.createOutputFile(this, expansionNodes, packsHttpNode)
        EncounterSetsJson.createOutputFile(this, expansionNodes, packsHttpNode)
        InvestigatorsJson.createOutputFile(this, expansionNodes, packsHttpNode)
        ProductsJson.createOutputFile(this, expansionNodes, packsHttpNode)

        expansionNodes.forEach { expansionNode ->
            val localExpansion = LocalExpansionFile.getBody(expansionNode)
            val expansionCode = expansionNode.inputPath.nameWithoutExtension

            // create files at /api/expansions/{slug}/{type}.json
            ExpansionJson.createOutputFile(this, expansionNodes, expansionCode, packsHttpNode, packHttpNodes)
            ExpansionScenariosJson.createOutputFile(this, expansionNodes, expansionCode, packsHttpNode, packHttpNodes)
            ExpansionEncounterSetsJson.createOutputFile(this, expansionNodes, expansionCode, packsHttpNode, packHttpNodes)
            ExpansionInvestigatorsJson.createOutputFile(this, expansionNodes, expansionCode, packsHttpNode, packHttpNodes)
            ExpansionProductsJson.createOutputFile(this, expansionNodes, expansionCode, packsHttpNode, packHttpNodes)

            // create files at /api/{type}/{id}.json
            localExpansion.scenarios.forEach { ScenarioJson.createOutputFile(this, expansionNodes, expansionCode, it.id, packsHttpNode, packHttpNodes) }
            localExpansion.encounterSets.forEach { EncounterSetJson.createOutputFile(this, expansionNodes, expansionCode, it.id, packsHttpNode, packHttpNodes) }
            localExpansion.investigators.forEach { InvestigatorJson.createOutputFile(this, expansionNodes, expansionCode, it.id, packsHttpNode, packHttpNodes) }
        }
    }
}
