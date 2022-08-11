package com.caseyjbrooks.arkham.ui.scenarios.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.CacheReady
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Column
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.bulma.Row
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.Scenario
import org.jetbrains.compose.web.dom.Text

object ScenariosUi {
    @Composable
    fun Content(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.scenariosViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: ScenariosContract.State, postInput: (ScenariosContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            Hero(
                title = { Text("Scenarios") },
                size = BulmaSize.Medium,
            )
            BulmaSection {
                Breadcrumbs(
                    NavigationRoute("Home", null, ArkhamApp.Home),
                    NavigationRoute("Scenarios", null, ArkhamApp.Scenarios),
                )
            }
            BulmaSection {
                CacheReady(state.layout, state.scenarios) { layoutState, scenarios ->
                    val regularExpansionsChunks = layoutState
                        .expansions
                        .filter { !it.isReturnTo }
                        .chunked(3)

                    regularExpansionsChunks.forEach { expansions ->
                        Row("features") {
                            expansions.forEach { expansion ->
                                Column("is-4") {
                                    val scenarios = expansion
                                        .scenarios
                                        .map { scenarioId ->
                                            scenarios.scenarios.single { it.id == scenarioId }
                                        }
                                    ExpansionCard(expansion, scenarios)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ExpansionCard(expansion: ExpansionLite, scenarios: List<Scenario>) {
        Card(
            imageUrl = expansion.boxArt,
            title = expansion.name,
            navigationRoutes = scenarios
                .map { scenario ->
                    NavigationRoute(
                        name = scenario.name,
                        iconUrl = scenario.icon,
                        route = ArkhamApp.ScenarioDetails,
                        params = arrayOf(scenario.id.id)
                    )
                }
                .toTypedArray()
        )
    }
}
