package com.caseyjbrooks.arkham.ui.products.list

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ProductList
import com.copperleaf.ballast.repository.cache.Cached

object ProductsContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val products: Cached<ProductList> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class ProductsUpdated(val products: Cached<ProductList>) : Inputs()
    }

    sealed class Events
}
