package com.caseyjbrooks.arkham.ui.pages

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import kotlinx.coroutines.CoroutineScope

class StaticPageViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: StaticPageInputHandler,
) : BasicViewModel<
    StaticPageContract.Inputs,
    StaticPageContract.Events,
    StaticPageContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .forViewModel(
            inputHandler = inputHandler,
            initialState = StaticPageContract.State(),
            name = "Static Page",
        ),
    eventHandler = eventHandler {  },
)
