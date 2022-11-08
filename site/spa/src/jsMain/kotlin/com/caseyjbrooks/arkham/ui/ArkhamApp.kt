package com.caseyjbrooks.arkham.ui

import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.route.RouteMatcher

enum class ArkhamApp(
    routeFormat: String,
    override val annotations: List<RouteAnnotation> = emptyList(),
) : Route {
    Home("/"),

    Expansions("/expansions"),
    ExpansionDetails("/expansions/{expansionCode}"),

    Investigators("/investigators"),
    InvestigatorDetails("/investigators/{investigatorId}"),

    Scenarios("/scenarios"),
    ScenarioDetails("/scenarios/{scenarioId}"),

    EncounterSets("/encounter-sets"),
    EncounterSetDetails("/encounter-sets/{encounterSetId}"),

    Products("/products"),
    ProductDetails("/products/{productId}"),

    StaticPage("/pages/{slug}"),

    Tools("/tools"),
    ChaosBagSimulator("/tools/chaos?scenarioId={?}"),
    InvestigatorTracker("/tools/investigator?investigatorId={?}"),
    AboutCampaignLog("/tools/campaign-log"),
    CreateCampaignLog("/tools/campaign-log/{expansionCode}"),
    ViewCampaignLog("/tools/campaign-log/{expansionCode}/{campaignLogId}"),
    DividersGenerator("/tools/dividers"),
    TuckboxGenerator("/tools/tuckbox"),
    CustomCards("/tools/cards");

    override val matcher: RouteMatcher = RouteMatcher.create(routeFormat)
}
