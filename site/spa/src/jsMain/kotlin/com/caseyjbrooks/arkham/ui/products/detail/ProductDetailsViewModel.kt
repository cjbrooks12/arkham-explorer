package com.caseyjbrooks.arkham.ui.products.detail

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope

class ProductDetailsViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: ProductDetailsInputHandler,
) : BasicViewModel<
    ProductDetailsContract.Inputs,
    ProductDetailsContract.Events,
    ProductDetailsContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = ProductDetailsContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler {  },
)
