package com.caseyjbrooks.arkham.ui.expansions.detail

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope

class ExpansionDetailsViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: ExpansionDetailsInputHandler,
) : BasicViewModel<
    ExpansionDetailsContract.Inputs,
    ExpansionDetailsContract.Events,
    ExpansionDetailsContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = ExpansionDetailsContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler {  },
)
