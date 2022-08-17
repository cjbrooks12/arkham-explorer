package com.caseyjbrooks.arkham.ui.home

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import kotlinx.coroutines.CoroutineScope

class HomeViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: HomeInputHandler,
) : BasicViewModel<
    HomeContract.Inputs,
    HomeContract.Events,
    HomeContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .forViewModel(
            inputHandler = inputHandler,
            initialState = HomeContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler {  },
)
