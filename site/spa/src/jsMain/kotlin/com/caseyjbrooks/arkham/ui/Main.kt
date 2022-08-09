package com.caseyjbrooks.arkham.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.bulma.MainNavBar
import com.caseyjbrooks.arkham.ui.encountersets.list.EncounterSetsUi
import com.caseyjbrooks.arkham.ui.expansions.detail.ExpansionDetailsUi
import com.caseyjbrooks.arkham.ui.expansions.list.ExpansionsUi
import com.caseyjbrooks.arkham.ui.home.HomeContract
import com.caseyjbrooks.arkham.ui.home.HomeUi
import com.caseyjbrooks.arkham.ui.investigators.detail.InvestigatorDetailsUi
import com.caseyjbrooks.arkham.ui.investigators.list.InvestigatorsUi
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsUi
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosUi
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.MissingDestination
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNotFound
import com.copperleaf.ballast.repository.cache.getCachedOrEmptyList
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Footer
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

val LocalInjector = staticCompositionLocalOf<ArkhamInjector> { error("LocalInjector not provided") }

@Composable
fun MainApplication(injector: ArkhamInjector) {
    val routerVm = remember { injector.routerViewModel() }
    val routerVmState by routerVm.observeStates().collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val vm = remember(coroutineScope, injector) { injector.homeViewModel(coroutineScope) }
    val vmState by vm.observeStates().collectAsState()

    when (val destination = routerVmState.currentDestinationOrNotFound) {
        is Destination -> {
            when (destination.originalRoute) {
                ArkhamApp.Home -> {
                    MainLayout(vmState, vm::trySend) {
                        HomeUi.Content(vmState) { vm.trySend(it) }
                    }
                }

                ArkhamApp.Expansions -> {
                    MainLayout(vmState, vm::trySend) {
                        ExpansionsUi.Content(injector)
                    }
                }

                ArkhamApp.ExpansionDetails -> {
                    MainLayout(vmState, vm::trySend) {
                        ExpansionDetailsUi.Content(
                            injector,
                            destination.pathParameters["expansionId"]!!.single(),
                        )
                    }
                }

                ArkhamApp.Investigators -> {
                    MainLayout(vmState, vm::trySend) {
                        InvestigatorsUi.Content(injector)
                    }
                }

                ArkhamApp.InvestigatorDetails -> {
                    MainLayout(vmState, vm::trySend) {
                        InvestigatorDetailsUi.Content(
                            injector,
                            destination.pathParameters["investigatorId"]!!.single(),
                        )
                    }
                }

                ArkhamApp.Scenarios -> {
                    MainLayout(vmState, vm::trySend) {
                        ScenariosUi.Content(injector)
                    }
                }

                ArkhamApp.ScenarioDetails -> {
                    MainLayout(vmState, vm::trySend) {
                        ScenarioDetailsUi.Content(
                            injector,
                            destination.pathParameters["scenarioId"]!!.single(),
                        )
                    }
                }

                ArkhamApp.EncounterSets -> {
                    MainLayout(vmState, vm::trySend) {
                        EncounterSetsUi.Content(injector)
                    }
                }

                ArkhamApp.EncounterSetDetails -> {
                    MainLayout(vmState, vm::trySend) {
                        ExpansionDetailsUi.Content(
                            injector,
                            destination.pathParameters["encounterSetId"]!!.single(),
                        )
                    }
                }

                ArkhamApp.Resources -> {
                    MainLayout(vmState, vm::trySend) {
                        Ul {
                            Li { Text("PlayingBoardGames YouTube Channel - https://www.youtube.com/c/PlayingBoardGames") }
                            Li { Text("ArkhamCards App - https://arkhamcards.com/") }
                            Li { Text("Dissonant Voices Narration - https://mythosbusters.com/2021/01/13/dissonant-voices-now-on-arkham-cards/") }
                            Li { Text("AHLCG Soundtrack - https://itswritingitself.wordpress.com/2020/01/28/ahlcg-arkham-horror-the-card-game-the-soundtrack/") }
                        }
                    }
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

@Composable
private fun MainLayout(
    state: HomeContract.State,
    postInput: (HomeContract.Inputs) -> Unit,
    content: @Composable () -> Unit
) {
    MainNavBar(
        homeRoute = ArkhamApp.Home,
        brandImageUrl = "http://arkhamcentral.com/wp-content/uploads/2017/05/ArkhamHorrorlogo.png",
        startNavigation = state.startNavigation.getCachedOrEmptyList(),
        endNavigation = state.endNavigation.getCachedOrEmptyList(),
    )
    content()
    BulmaFooter()
}

@Composable
fun BulmaFooter() {
    Footer({ classes("footer") }) {
        Div({ classes("content", "has-text-centered") }) {
            P { Text("The information presented on this site about Arkham Horror: The Card Game, both literal and graphical, is copyrighted by Fantasy Flight Games. This website is not produced, endorsed, supported, or affiliated with Fantasy Flight Games.") }
        }
    }
}
