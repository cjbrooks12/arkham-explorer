package com.caseyjbrooks.arkham.ui.error

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope

class NavigationErrorViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: NavigationErrorInputHandler,
) : BasicViewModel<
    NavigationErrorContract.Inputs,
    NavigationErrorContract.Events,
    NavigationErrorContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = NavigationErrorContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler {  },
)
