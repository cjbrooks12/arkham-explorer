package com.caseyjbrooks.arkham.ui.products.detail

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
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.Product
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
    fun Header(expansion: ExpansionLite, product: Product) {
        Hero(
            title = { Text(product.name) },
            subtitle = { Text(expansion.name) },
            size = BulmaSize.Small,
            classes = listOf("special"),
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ArkhamApp.Home),
                NavigationRoute("Expansions", null, ArkhamApp.Expansions),
                NavigationRoute(expansion.name, expansion.icon, ArkhamApp.ExpansionDetails, expansion.code),
                NavigationRoute(product.name, null, ArkhamApp.ProductDetails, product.id.id),
            )
        }
    }

    @Composable
    fun Body(expansion: ExpansionLite, product: Product) {

    }
}
