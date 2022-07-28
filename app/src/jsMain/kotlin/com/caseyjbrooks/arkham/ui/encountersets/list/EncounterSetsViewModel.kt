package com.caseyjbrooks.arkham.ui.encountersets.list

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope

class EncounterSetsViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: EncounterSetsInputHandler,
) : BasicViewModel<
    EncounterSetsContract.Inputs,
    EncounterSetsContract.Events,
    EncounterSetsContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = EncounterSetsContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler {  },
)
