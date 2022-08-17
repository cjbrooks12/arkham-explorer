package com.caseyjbrooks.arkham.ui.scenarios.list

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import kotlinx.coroutines.CoroutineScope

class ScenariosViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: ScenariosInputHandler,
) : BasicViewModel<
    ScenariosContract.Inputs,
    ScenariosContract.Events,
    ScenariosContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .forViewModel(
            inputHandler = inputHandler,
            initialState = ScenariosContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler {  }
)
