package com.caseyjbrooks.arkham.ui.encountersets.detail

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
import com.caseyjbrooks.arkham.utils.theme.color
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.arkham.models.api.EncounterSetDetails
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.ExpansionLite
import org.jetbrains.compose.web.dom.Text

@Suppress("UNUSED_PARAMETER")
object EncounterSetDetailsUi {
    @Composable
    fun Page(injector: ArkhamInjector, encounterSetId: EncounterSetId) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, encounterSetId) { injector.encounterSetDetailsViewModel(coroutineScope, encounterSetId) }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: EncounterSetDetailsContract.State, postInput: (EncounterSetDetailsContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            CacheReady(
                state.parentExpansion,
                state.encounterSet,
            ) { expansion, encounterSet ->
                Header(expansion, encounterSet)
                Body(expansion, encounterSet)
            }
        }
    }

    @Composable
    fun Header(expansion: ExpansionLite, encounterSet: EncounterSetDetails) {
        Hero(
            title = { Text(encounterSet.name) },
            subtitle = { Text(expansion.name) },
            size = BulmaSize.Small,
            classes = listOf("special"),
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ArkhamApp.Home),
                NavigationRoute("Expansions", null, ArkhamApp.Expansions),
                NavigationRoute(expansion.name, expansion.icon, ArkhamApp.ExpansionDetails, expansion.expansionCode),
                NavigationRoute(encounterSet.name, encounterSet.icon, ArkhamApp.EncounterSetDetails, encounterSet.id.id),
            )
        }
    }

    @Composable
    fun Body(expansion: ExpansionLite, encounterSet: EncounterSetDetails) {
        DynamicGrid(
            GridItem {
                Card(title = "Details")
            },
            GridItem {
                Card(title = "Tools")
            },
            GridItem {
                Card(
                    title = "Products",
                    description = "${encounterSet.name} is available in the following products:",
                    navigationRoutes = encounterSet.products.map { product ->
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
        )
    }
}
