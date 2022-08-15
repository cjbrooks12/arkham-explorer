package com.caseyjbrooks.arkham.ui.expansions.detail

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
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaColor
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.arkham.models.api.Expansion
import kotlinx.serialization.json.JsonNull
import org.jetbrains.compose.web.dom.Text

object ExpansionDetailsUi {
    @Composable
    fun Page(injector: ArkhamInjector, expansionCode: String) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, expansionCode) {
            injector.expansionDetailsViewModel(
                coroutineScope,
                expansionCode
            )
        }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: ExpansionDetailsContract.State, postInput: (ExpansionDetailsContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            CacheReady(state.expansion) { expansion ->
                Header(expansion)
                Body(expansion)
            }
        }
    }

    @Composable
    fun Header(expansion: Expansion) {
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
                NavigationRoute(expansion.name, expansion.icon, ArkhamApp.ExpansionDetails, expansion.expansionCode),
            )
        }
    }

    @Composable
    fun Body(expansion: Expansion) {
        DynamicGrid(
            GridItem {
                Card(
                    title = "Scenarios",
                    navigationRoutes = expansion
                        .scenarios
                        .map { scenario ->
                            NavigationRoute(
                                name = scenario.name,
                                iconUrl = scenario.icon,
                                route = ArkhamApp.ScenarioDetails,
                                pathParams = arrayOf(scenario.id.id),
                                buttonColor = BulmaColor.Primary,
                            )
                        }
                        .toTypedArray()
                )
            },
            GridItem {
                Card(
                    title = "Encounter Sets",
                    navigationRoutes = expansion
                        .encounterSets
                        .map { encounterSet ->
                            NavigationRoute(
                                name = encounterSet.name,
                                iconUrl = encounterSet.icon,
                                route = ArkhamApp.EncounterSetDetails,
                                pathParams = arrayOf(encounterSet.id.id),
                                buttonColor = BulmaColor.Primary,
                            )
                        }
                        .toTypedArray()
                )
            },
            GridItem {
                Card(
                    title = "Investigators",
                    navigationRoutes = expansion
                        .investigators
                        .map { investigator ->
                            NavigationRoute(
                                name = investigator.name,
                                iconUrl = null,
                                route = ArkhamApp.InvestigatorDetails,
                                pathParams = arrayOf(investigator.id.id),
                                buttonColor = BulmaColor.Primary,
                            )
                        }
                        .toTypedArray()
                )
            },
            GridItem {
                Card(
                    title = "Products",
                    navigationRoutes = expansion
                        .products
                        .map { product ->
                            NavigationRoute(
                                name = product.name,
                                iconUrl = null,
                                route = ArkhamApp.ProductDetails,
                                pathParams = arrayOf(product.id.id),
                                buttonColor = BulmaColor.Primary,
                            )
                        }
                        .toTypedArray()
                )
            },
            GridItem {
                Card(
                    title = "Tools",
                    navigationRoutes = buildList<NavigationRoute> {
                        if (expansion.campaignLogSchema != JsonNull) {
                            this += NavigationRoute("Campaign log", null, ArkhamApp.CreateCampaignLog, expansion.expansionCode)
                        }
                    }.toTypedArray()
                )
            },
        )
    }
}
