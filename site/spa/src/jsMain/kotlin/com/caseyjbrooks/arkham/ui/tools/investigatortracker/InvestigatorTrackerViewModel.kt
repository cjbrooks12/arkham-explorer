package com.caseyjbrooks.arkham.ui.tools.investigatortracker

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import kotlinx.coroutines.CoroutineScope

class InvestigatorTrackerViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: InvestigatorTrackerInputHandler,
) : BasicViewModel<
    InvestigatorTrackerContract.Inputs,
    InvestigatorTrackerContract.Events,
    InvestigatorTrackerContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .forViewModel(
            inputHandler = inputHandler,
            initialState = InvestigatorTrackerContract.State(),
            name = "Investigator Tracker",
        ),
    eventHandler = eventHandler { },
)
