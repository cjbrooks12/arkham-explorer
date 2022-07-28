package com.caseyjbrooks.arkham.ui.investigators.detail

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope

class InvestigatorDetailsViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: InvestigatorDetailsInputHandler,
) : BasicViewModel<
    InvestigatorDetailsContract.Inputs,
    InvestigatorDetailsContract.Events,
    InvestigatorDetailsContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = InvestigatorDetailsContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler { },
)
