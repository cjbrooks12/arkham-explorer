package com.caseyjbrooks.arkham.ui.products.detail

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class ProductDetailsInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    ProductDetailsContract.Inputs,
    ProductDetailsContract.Events,
    ProductDetailsContract.State,
    > {
    override suspend fun InputHandlerScope<ProductDetailsContract.Inputs, ProductDetailsContract.Events, ProductDetailsContract.State>.handleInput(
        input: ProductDetailsContract.Inputs
    ) = when (input) {
        is ProductDetailsContract.Inputs.Initialize -> {
            updateState { it.copy(productId = input.productId) }
            observeFlows(
                "Product Details",
                repository
                    .getExpansions(false)
                    .map { ProductDetailsContract.Inputs.ExpansionsUpdated(it) },
                repository
                    .getProduct(false, input.productId)
                    .map { ProductDetailsContract.Inputs.ProductUpdated(it) }
            )
        }

        is ProductDetailsContract.Inputs.ExpansionsUpdated -> {
            updateState { state -> state.copy(layout = MainLayoutState.fromCached(input.expansions)) }
        }

        is ProductDetailsContract.Inputs.ProductUpdated -> {
            updateState { it.copy(product = input.product) }
        }
    }
}
