package com.caseyjbrooks.arkham.ui.chaosbag

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope

class ChaosBagSimulatorViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: ChaosBagSimulatorInputHandler,
) : BasicViewModel<
    ChaosBagSimulatorContract.Inputs,
    ChaosBagSimulatorContract.Events,
    ChaosBagSimulatorContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = ChaosBagSimulatorContract.State(),
            name = "Chaos Bag Simulator",
        ),
    eventHandler = eventHandler { },
)
