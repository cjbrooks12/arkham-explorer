package com.caseyjbrooks.arkham.ui.expansions.list

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope

class ExpansionsViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: ExpansionsInputHandler,
) : BasicViewModel<
    ExpansionsContract.Inputs,
    ExpansionsContract.Events,
    ExpansionsContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = ExpansionsContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler {  },
)
