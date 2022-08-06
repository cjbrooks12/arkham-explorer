package com.caseyjbrooks.arkham.ui.home

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.withRepository
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
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = HomeContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler {  },
)
