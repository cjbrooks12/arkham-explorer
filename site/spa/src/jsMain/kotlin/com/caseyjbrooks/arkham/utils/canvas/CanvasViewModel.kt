package com.caseyjbrooks.arkham.utils.canvas

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope

class CanvasViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: CanvasInputHandler,
) : BasicViewModel<
    CanvasContract.Inputs,
    CanvasContract.Events,
    CanvasContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withViewModel(
            initialState = CanvasContract.State(),
            inputHandler = inputHandler,
            name = "Custom Cards",
        )
        .build(),
    eventHandler = eventHandler { },
)
