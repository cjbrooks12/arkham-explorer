package com.caseyjbrooks.arkham.ui

import com.copperleaf.ballast.navigation.routing.RoutingTable

object ArkhamApp : RoutingTable() {
    val Main = route("/")

    val Expansions = route("/expansions")
    val ExpansionDetails = route("/expansions/{expansionName}")

    val EncounterSets = route("/encounter-sets")
    val EncounterSetDetails = route("/encounter-sets/{encounterSetName}")
}
