package com.caseyjbrooks.arkham.ui.products.detail

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.Product
import com.copperleaf.arkham.models.api.ProductId
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.map

object ProductDetailsContract {
    data class State(
        val productId: ProductId = ProductId(""),
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val product: Cached<Product> = Cached.NotLoaded(),
    ) {
        val parentExpansion: Cached<ExpansionLite> = layout
            .map { expansions ->
                expansions.expansions.single { expansion ->
                    expansion.code == product.getCachedOrNull()?.expansionCode
                }
            }
    }

    sealed class Inputs {
        data class Initialize(val productId: ProductId) : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class ProductUpdated(val product: Cached<Product>) : Inputs()
    }

    sealed class Events
}
