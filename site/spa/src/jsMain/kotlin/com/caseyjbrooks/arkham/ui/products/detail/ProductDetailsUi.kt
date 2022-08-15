package com.caseyjbrooks.arkham.ui.products.detail

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
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.ProductDetails
import com.copperleaf.arkham.models.api.ProductId
import org.jetbrains.compose.web.dom.Text

object ProductDetailsUi {
    @Composable
    fun Page(injector: ArkhamInjector, productId: ProductId) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, productId) {
            injector.productDetailsViewModel(
                coroutineScope,
                productId
            )
        }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: ProductDetailsContract.State, postInput: (ProductDetailsContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            CacheReady(state.parentExpansion, state.product) { expansion, product ->
                Header(expansion, product)
                Body(expansion, product)
            }
        }
    }

    @Composable
    fun Header(expansion: ExpansionLite, product: ProductDetails) {
        Hero(
            title = { Text(product.name) },
            subtitle = { Text(expansion.name) },
            size = BulmaSize.Small,
            classes = listOf("special"),
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ArkhamApp.Home),
                NavigationRoute("Products", null, ArkhamApp.Products),
                NavigationRoute(expansion.name, expansion.icon, ArkhamApp.ExpansionDetails, expansion.expansionCode),
                NavigationRoute(product.name, null, ArkhamApp.ProductDetails, product.id.id),
            )
        }
    }

    @Composable
    fun Body(expansion: ExpansionLite, product: ProductDetails) {
        DynamicGrid(
            GridItem {
                Card(
                    title = "Scenarios",
                    navigationRoutes = product
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
                    navigationRoutes = product
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
                    navigationRoutes = product
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
        )
    }
}
