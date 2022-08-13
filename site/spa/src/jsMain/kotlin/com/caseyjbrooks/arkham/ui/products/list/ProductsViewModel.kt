package com.caseyjbrooks.arkham.ui.products.list

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope

class ProductsViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: ProductsInputHandler,
) : BasicViewModel<
    ProductsContract.Inputs,
    ProductsContract.Events,
    ProductsContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = ProductsContract.State(),
            name = "Products",
        ),
    eventHandler = eventHandler {  }
)
