package com.caseyjbrooks.arkham.ui

import com.copperleaf.ballast.BallastViewModel
import com.copperleaf.ballast.navigation.routing.RouterContract

typealias RouterViewModel = BallastViewModel<
    RouterContract.Inputs,
    RouterContract.Events,
    RouterContract.State
    >
