package com.caseyjbrooks.arkham.ui

import com.copperleaf.ballast.navigation.routing.RoutingTable

object ArkhamApp : RoutingTable() {
    val Home = route("/")

    val Expansions = route("/expansions")
    val ExpansionDetails = route("/expansions/{expansionCode}")

    val Investigators = route("/investigators")
    val InvestigatorDetails = route("/investigators/{investigatorId}")

    val Scenarios = route("/scenarios")
    val ScenarioDetails = route("/scenarios/{scenarioId}")

    val EncounterSets = route("/encounter-sets")
    val EncounterSetDetails = route("/encounter-sets/{encounterSetId}")

    val Products = route("/products")
    val ProductDetails = route("/products/{productId}")

    val StaticPage = route("/pages/{slug}")

    val Tools = route("/tools")
    val ChaosBagSimulator = route("/tools/chaos")
    val AboutCampaignLog = route("/tools/campaign-log")
    val CreateCampaignLog = route("/tools/campaign-log/{expansionCode}")
    val ViewCampaignLog = route("/tools/campaign-log/{expansionCode}/{campaignLogId}")
    val DividersGenerator = route("/tools/dividers")
    val TuckboxGenerator = route("/tools/tuckbox")
    val CustomCards = route("/tools/cards")
}
