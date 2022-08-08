package com.caseyjbrooks.arkham.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.models.NavigationSection
import com.caseyjbrooks.arkham.ui.encountersets.list.EncounterSetsUi
import com.caseyjbrooks.arkham.ui.expansions.detail.ExpansionDetailsUi
import com.caseyjbrooks.arkham.ui.expansions.list.ExpansionsUi
import com.caseyjbrooks.arkham.ui.home.HomeContract
import com.caseyjbrooks.arkham.ui.home.HomeUi
import com.caseyjbrooks.arkham.ui.investigators.detail.InvestigatorDetailsUi
import com.caseyjbrooks.arkham.ui.investigators.list.InvestigatorsUi
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsUi
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosUi
import com.caseyjbrooks.arkham.utils.navigation.NavigationLink
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.MissingDestination
import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNotFound
import com.copperleaf.ballast.repository.cache.getCachedOrEmptyList
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Span
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
                    ExpansionDetailsUi.Content(
                        injector,
                        destination.pathParameters["encounterSetId"]!!.single(),
                    )
                }

                ArkhamApp.Resources -> {
                    Ul {
                        Li { Text("PlayingBoardGames YouTube Channel - https://www.youtube.com/c/PlayingBoardGames") }
                        Li { Text("ArkhamCards App - https://arkhamcards.com/") }
                        Li { Text("Dissonant Voices Narration - https://mythosbusters.com/2021/01/13/dissonant-voices-now-on-arkham-cards/") }
                        Li { Text("AHLCG Soundtrack - https://itswritingitself.wordpress.com/2020/01/28/ahlcg-arkham-horror-the-card-game-the-soundtrack/") }
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
    Section({ classes("section") }) {
        Div({ classes("container") }) {
            content()
        }
    }
}

@Composable
private fun MainNavBar(
    homeRoute: Route,
    brandImageUrl: String,
    startNavigation: List<NavigationSection>,
    endNavigation: List<NavigationSection>
) {
    Nav(attrs = {
        classes("navbar", "is-success")
        attr("role", "navigation")
        attr("aria-label", "main navigation")
    }) {
        var mobileNavbarOpen by remember { mutableStateOf(false) }

        Div(attrs = {
            classes("navbar-brand")
        }) {
            NavigationLink(ArkhamApp.Home, classes = listOf("navbar-item")) {
                Img(
                    attrs = {
                        attr("height", "28")
                    },
                    src = brandImageUrl
                )
            }

            A(
                attrs = {
                    if(mobileNavbarOpen) {
                        classes("navbar-burger", "is-active")
                    } else {
                        classes("navbar-burger")
                    }
                    attr("role", "navigation")
                    attr("aria-label", "menu")
                    attr("aria-expanded", "false")
                    attr("data-target", "navbarMain")
                    onClick { mobileNavbarOpen = !mobileNavbarOpen }
                }
            ) {
                Span(attrs = { attr("aria-hidden", "true") })
                Span(attrs = { attr("aria-hidden", "true") })
                Span(attrs = { attr("aria-hidden", "true") })
            }
        }

        Div(attrs = {
            if(mobileNavbarOpen) {
                classes("navbar-menu", "is-active")
            } else {
                classes("navbar-menu")
            }
            id("navbarMain")
        }) {
            Div(attrs = {
                classes("navbar-start")
            }) {
                NavbarSection(startNavigation)
            }
            Div(attrs = {
                classes("navbar-end")
            }) {
                NavbarSection(endNavigation)
            }
        }
    }
}

@Composable
private fun NavbarSection(
    sections: List<NavigationSection>,
) {
    sections.forEach { section ->
        if (section.routes.size == 1) {
            NavigationLink(
                route = section.routes.single().route,
                pathParameters = section.routes.single().params,
                classes = listOf("navbar-item"),
            ) { Text(section.name) }
        } else {
            var isExpanded by remember { mutableStateOf(false) }
            Div({
                if(isExpanded) {
                    classes("navbar-item", "has-dropdown", "is-active")
                } else {
                    classes("navbar-item", "has-dropdown")
                }
            }) {
                A(attrs = {
                    classes("navbar-link")
                    onClick { isExpanded = !isExpanded }
                }) {
                    Text(section.name)
                }

                Div({ classes("navbar-dropdown") }) {
                    section.routes.forEach {
                        NavigationLink(it.route, pathParameters = it.params, classes = listOf("navbar-item")) {
                            Text(it.name)
                        }
                    }
                }
            }
        }
    }
}
