package com.caseyjbrooks.arkham.ui.encountersets.detail

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import kotlinx.coroutines.CoroutineScope

class EncounterSetDetailsViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: EncounterSetDetailsInputHandler,
) : BasicViewModel<
    EncounterSetDetailsContract.Inputs,
    EncounterSetDetailsContract.Events,
    EncounterSetDetailsContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .forViewModel(
            inputHandler = inputHandler,
            initialState = EncounterSetDetailsContract.State(),
            name = "Arkham Explorer",
        ),
    eventHandler = eventHandler {  },
)
