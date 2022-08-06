package com.caseyjbrooks.arkham.ui

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.navigation.routing.RouterContract
import kotlinx.coroutines.CoroutineScope

class RouterViewModel(
    config: BallastViewModelConfiguration.Builder,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
    RouterContract.Inputs,
    RouterContract.Events,
    RouterContract.State
>(
    config = config.build(),
    eventHandler = eventHandler {  },
    coroutineScope = coroutineScope,
)
