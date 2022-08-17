package com.caseyjbrooks.arkham.ui.tools.cards

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import kotlinx.coroutines.CoroutineScope

class CustomCardsViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: CustomCardsInputHandler,
) : BasicViewModel<
    CustomCardsContract.Inputs,
    CustomCardsContract.Events,
    CustomCardsContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .forViewModel(
            inputHandler = inputHandler,
            initialState = CustomCardsContract.State(),
            name = "Custom Cards",
        ),
    eventHandler = eventHandler { },
)
