package com.caseyjbrooks.arkham.ui

import com.copperleaf.ballast.navigation.routing.RoutingTable

object ArkhamApp : RoutingTable() {
    val Home = route("/")

    val Expansions = route("/expansions")
    val ExpansionDetails = route("/expansions/{expansionId}")

    val Investigators = route("/investigators")
    val InvestigatorDetails = route("/investigators/{investigatorId}")

    val Scenarios = route("/scenarios")
    val ScenarioDetails = route("/scenarios/{scenarioId}")

    val EncounterSets = route("/encounter-sets")
    val EncounterSetDetails = route("/encounter-sets/{encounterSetId}")

    val Resources = route("/resources")
}
