package com.caseyjbrooks.arkham.repository.main

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.forViewModel
import kotlinx.coroutines.CoroutineScope

class ArkhamExplorerViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: ArkhamExplorerInputHandler,
    eventHandler: ArkhamExplorerEventHandler,
) : BasicViewModel<
    ArkhamExplorerContract.Inputs,
    ArkhamExplorerContract.Events,
    ArkhamExplorerContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            inputStrategy = FifoInputStrategy()
//            logger = { PrintlnLogger(it) }
//            this += LoggingInterceptor()
        }
        .forViewModel(
            inputHandler = inputHandler,
            initialState = ArkhamExplorerContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler,
)
