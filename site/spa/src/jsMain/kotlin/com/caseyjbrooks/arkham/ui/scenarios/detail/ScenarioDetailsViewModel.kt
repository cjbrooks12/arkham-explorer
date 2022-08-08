package com.caseyjbrooks.arkham.ui.scenarios.detail

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope

class ScenarioDetailsViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: ScenarioDetailsInputHandler,
) : BasicViewModel<
    ScenarioDetailsContract.Inputs,
    ScenarioDetailsContract.Events,
    ScenarioDetailsContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = ScenarioDetailsContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler {  },
)