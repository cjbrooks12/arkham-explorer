package com.caseyjbrooks.arkham.ui.expansions.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.CacheReady
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaColor
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Column
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.bulma.Row
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import org.jetbrains.compose.web.dom.Text

object ExpansionDetailsUi {
    @Composable
    fun Content(injector: ArkhamInjector, expansionId: String) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, expansionId) {
            injector.expansionDetailsViewModel(
                coroutineScope,
                expansionId
            )
        }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: ExpansionDetailsContract.State, postInput: (ExpansionDetailsContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            CacheReady(
                state.expansion
            ) { expansion ->
                Hero(
                    title = { Text(expansion.name) },
                    subtitle = { Text("Expansion") },
                    size = BulmaSize.Small,
                    classes = listOf("special"),
                )
                BulmaSection {
                    Breadcrumbs(
                        NavigationRoute("Home", null, ArkhamApp.Home),
                        NavigationRoute("Expansions", null, ArkhamApp.Expansions),
                        NavigationRoute(expansion.name, expansion.icon, ArkhamApp.ExpansionDetails, expansion.code),
                    )
                }

                BulmaSection {
                    Row {
                        Column {
                            Card(
                                title = "Scenarios",
                                navigationRoutes = expansion
                                    .scenarios
                                    .map { scenario ->
                                        NavigationRoute(
                                            name = scenario.name,
                                            iconUrl = scenario.icon,
                                            route = ArkhamApp.ScenarioDetails,
                                            params = arrayOf(scenario.id.id),
                                            buttonColor = BulmaColor.Primary,
                                        )
                                    }
                                    .toTypedArray()
                            )
                        }
                        Column {
                            Card(
                                title = "Encounter Sets",
                                navigationRoutes = expansion
                                    .encounterSets
                                    .map { encounterSet ->
                                        NavigationRoute(
                                            name = encounterSet.name,
                                            iconUrl = encounterSet.icon,
                                            route = ArkhamApp.EncounterSetDetails,
                                            params = arrayOf(encounterSet.id.id),
                                            buttonColor = BulmaColor.Primary,
                                        )
                                    }
                                    .toTypedArray()
                            )
                        }
                    }
                    Row {
                        Column {
                            Card(
                                title = "Investigators",
                            )
                        }
                        Column {
                            Card(
                                title = "Products",
                            )
                        }
                        Column {
                            Card(
                                title = "Tools",
                            )
                        }
                    }
                }
            }
        }
    }
}
