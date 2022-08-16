package com.caseyjbrooks.arkham.ui.scenarios.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.CacheReady
import com.caseyjbrooks.arkham.utils.DynamicGrid
import com.caseyjbrooks.arkham.utils.GridItem
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.ScenarioLite
import org.jetbrains.compose.web.dom.Text

object ScenariosUi {
    @Composable
    fun Page(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.scenariosViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: ScenariosContract.State, postInput: (ScenariosContract.Inputs) -> Unit) {
        MainLayout(state.layout) { layoutState ->
            Header()
            CacheReady(state.scenarios) { scenarios ->
                Body(layoutState, scenarios.scenarios)
            }
        }
    }

    @Composable
    fun Header() {
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
    }

    @Composable
    fun Body(layoutState: MainLayoutState, scenarios: List<ScenarioLite>) {
        DynamicGrid(
            layoutState.expansions.map { expansion ->
                GridItem {
                    ExpansionCard(
                        expansion,
                        scenarios
                            .filter { it.expansionCode == expansion.expansionCode }
                    )
                }
            }
        )
    }

    @Composable
    private fun ExpansionCard(expansion: ExpansionLite, scenarios: List<ScenarioLite>) {
        Card(
            imageUrl = expansion.boxArt,
            title = expansion.name,
            navigationRoutes = scenarios
                .map { scenario ->
                    NavigationRoute(
                        name = scenario.name,
                        iconUrl = scenario.icon,
                        route = ArkhamApp.ScenarioDetails,
                        pathParams = arrayOf(scenario.id.id)
                    )
                }
                .toTypedArray()
        )
    }
}
