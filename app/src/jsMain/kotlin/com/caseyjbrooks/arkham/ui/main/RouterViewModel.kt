package com.caseyjbrooks.arkham.ui.main

import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.navigation.routing.BrowserHashNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.withRouter
import com.copperleaf.ballast.plusAssign
import kotlinx.coroutines.CoroutineScope

class RouterViewModel(
    config: BallastViewModelConfiguration.Builder,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
    RouterContract.Inputs,
    RouterContract.Events,
    RouterContract.State
>(
    config = config
        .withRouter(ArkhamApp)
        .apply {
            this += BrowserHashNavigationInterceptor()
        }
        .build(),
    eventHandler = eventHandler {  },
    coroutineScope = coroutineScope,
)
