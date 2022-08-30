package com.caseyjbrooks.arkham.ui.scenarios.detail

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
import com.caseyjbrooks.arkham.utils.theme.DownloadIconsCard
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.color
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.caseyjbrooks.arkham.utils.theme.tooltip
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.ScenarioDetails
import com.copperleaf.arkham.models.api.ScenarioId
import org.jetbrains.compose.web.dom.Text

@Suppress("UNUSED_PARAMETER")
object ScenarioDetailsUi {
    @Composable
    fun Page(injector: ArkhamInjector, scenarioId: ScenarioId) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, scenarioId) {
            injector.scenarioDetailsViewModel(
                coroutineScope,
                scenarioId
            )
        }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: ScenarioDetailsContract.State, postInput: (ScenarioDetailsContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            CacheReady(state.parentExpansion, state.scenario) { expansion, scenario ->
                Header(expansion, scenario)
                Body(expansion, scenario)
            }
        }
    }

    @Composable
    fun Header(expansion: ExpansionLite, scenario: ScenarioDetails) {
        Hero(
            title = { Text(scenario.name) },
            subtitle = { Text(expansion.name) },
            size = BulmaSize.Small,
            classes = listOf("special"),
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ArkhamApp.Home),
                NavigationRoute("Expansions", null, ArkhamApp.Expansions),
                NavigationRoute(expansion.name, expansion.icon, ArkhamApp.ExpansionDetails, expansion.expansionCode),
                NavigationRoute(scenario.name, scenario.icon, ArkhamApp.ScenarioDetails, scenario.id.id),
            )
        }
    }

    @Composable
    fun Body(expansion: ExpansionLite, scenario: ScenarioDetails) {
        DynamicGrid(
            GridItem {
                Card(
                    title = "Encounter Sets",
                    navigationRoutes = scenario
                        .encounterSets
                        .map { encounterSet ->
                            NavigationRoute(
                                name = encounterSet.encounterSet.name,
                                iconUrl = encounterSet.encounterSet.icon,
                                route = ArkhamApp.EncounterSetDetails,
                                pathParams = arrayOf(encounterSet.encounterSet.id.id),
                                buttonColor = encounterSet.color,
                                tooltip = encounterSet.tooltip,
                            )
                        }
                        .toTypedArray()
                )
            },
            GridItem {
                Card(
                    title = "Products",
                    description = "${scenario.name} is available in the following products:",
                    navigationRoutes = scenario.products.map { product ->
                        NavigationRoute(
                            name = product.name,
                            iconUrl = null,
                            route = ArkhamApp.ProductDetails,
                            pathParams = arrayOf(product.id.id),
                            buttonColor = product.productType.color,
                            tooltip = product.productType.name,
                        )
                    }.toTypedArray()
                )
            },
            GridItem {
                Card(
                    title = "Tools",
                    navigationRoutes = buildList<NavigationRoute> {
                        this += NavigationRoute("Campaign log", null, ArkhamApp.CreateCampaignLog, expansion.expansionCode)
                        this += NavigationRoute(
                            "Chaos Bag",
                            null,
                            ArkhamApp.ChaosBagSimulator,
                            queryParams = mapOf("scenarioId" to listOf(scenario.id.id))
                        )
                    }.toTypedArray()
                )
            },
            GridItem {
                DownloadIconsCard(expansion.icon)
            },
        )
    }
}
