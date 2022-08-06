package com.caseyjbrooks.arkham.ui.investigators.list

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope

class InvestigatorsViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: InvestigatorsInputHandler,
) : BasicViewModel<
    InvestigatorsContract.Inputs,
    InvestigatorsContract.Events,
    InvestigatorsContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = InvestigatorsContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler {  },
)
