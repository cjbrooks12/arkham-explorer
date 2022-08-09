package com.caseyjbrooks.arkham.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.cards.CustomCardsDesignerUi
import com.caseyjbrooks.arkham.ui.chaosbag.ChaosBagSimulatorUi
import com.caseyjbrooks.arkham.ui.dividers.DividersGeneratorUi
import com.caseyjbrooks.arkham.ui.encountersets.detail.EncounterSetDetailsUi
import com.caseyjbrooks.arkham.ui.encountersets.list.EncounterSetsUi
import com.caseyjbrooks.arkham.ui.expansions.detail.ExpansionDetailsUi
import com.caseyjbrooks.arkham.ui.expansions.list.ExpansionsUi
import com.caseyjbrooks.arkham.ui.home.HomeUi
import com.caseyjbrooks.arkham.ui.investigators.detail.InvestigatorDetailsUi
import com.caseyjbrooks.arkham.ui.investigators.list.InvestigatorsUi
import com.caseyjbrooks.arkham.ui.pages.StaticPageUi
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsUi
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosUi
import com.caseyjbrooks.arkham.ui.tools.ToolsUi
import com.caseyjbrooks.arkham.ui.tuckbox.TuckboxGeneratorUi
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.MissingDestination
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNotFound

val LocalInjector = staticCompositionLocalOf<ArkhamInjector> { error("LocalInjector not provided") }

@Composable
fun MainApplication(injector: ArkhamInjector) {
    val routerVm = remember { injector.routerViewModel() }
    val routerVmState by routerVm.observeStates().collectAsState()

    LaunchedEffect(routerVmState) {
        println("router state changed")
    }
    val destination = routerVmState.currentDestinationOrNotFound
    LaunchedEffect(destination) {
        println("destination changed")
    }

    when (destination) {
        is Destination -> {
            when (destination.originalRoute) {
                ArkhamApp.Home -> {
                    HomeUi.Content(injector)
                }

                ArkhamApp.Expansions -> {
                    ExpansionsUi.Content(injector)
                }

                ArkhamApp.ExpansionDetails -> {
                    ExpansionDetailsUi.Content(
                        injector,
                        destination.pathParameters["expansionId"]!!.single(),
                    )
                }

                ArkhamApp.Investigators -> {
                    InvestigatorsUi.Content(injector)
                }

                ArkhamApp.InvestigatorDetails -> {
                    InvestigatorDetailsUi.Content(
                        injector,
                        destination.pathParameters["investigatorId"]!!.single(),
                    )
                }

                ArkhamApp.Scenarios -> {
                    ScenariosUi.Content(injector)
                }

                ArkhamApp.ScenarioDetails -> {
                    ScenarioDetailsUi.Content(
                        injector,
                        destination.pathParameters["scenarioId"]!!.single(),
                    )
                }

                ArkhamApp.EncounterSets -> {
                    EncounterSetsUi.Content(injector)
                }

                ArkhamApp.EncounterSetDetails -> {
                    EncounterSetDetailsUi.Content(
                        injector,
                        destination.pathParameters["encounterSetId"]!!.single(),
                    )
                }

                ArkhamApp.StaticPage -> {
                    StaticPageUi.Content(
                        injector,
                        destination.pathParameters["slug"]!!.single(),
                    )
                }

                ArkhamApp.Tools -> {
                    ToolsUi.Content(injector)
                }

                ArkhamApp.ChaosBagSimulator -> {
                    ChaosBagSimulatorUi.Content(injector)
                }

                ArkhamApp.DividersGenerator -> {
                    DividersGeneratorUi.Content(injector)
                }

                ArkhamApp.TuckboxGenerator -> {
                    TuckboxGeneratorUi.Content(injector)
                }

                ArkhamApp.CustomCards -> {
                    CustomCardsDesignerUi.Content(injector)
                }
            }
        }

        is MissingDestination -> {
            HomeUi.NotFound(destination.originalUrl)
        }

        else -> {
            HomeUi.UnknownError()
        }
    }
}

