package com.caseyjbrooks.arkham.ui.products.list

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class ProductsInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    ProductsContract.Inputs,
    ProductsContract.Events,
    ProductsContract.State,
    > {
    override suspend fun InputHandlerScope<ProductsContract.Inputs, ProductsContract.Events, ProductsContract.State>.handleInput(
        input: ProductsContract.Inputs
    ) = when (input) {
        is ProductsContract.Inputs.Initialize -> {
            observeFlows(
                "Products",
                repository
                    .getExpansions(false)
                    .map { ProductsContract.Inputs.ExpansionsUpdated(it) },
                repository
                    .getProducts(false)
                    .map { ProductsContract.Inputs.ProductsUpdated(it) }
            )
        }

        is ProductsContract.Inputs.ExpansionsUpdated -> {
            updateState { it.copy(layout = MainLayoutState.fromCached(input.expansions)) }
        }

        is ProductsContract.Inputs.ProductsUpdated -> {
            updateState { it.copy(products = input.products) }
        }
    }
}
