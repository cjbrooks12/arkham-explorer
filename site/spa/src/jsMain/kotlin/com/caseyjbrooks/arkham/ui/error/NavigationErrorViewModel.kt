package com.caseyjbrooks.arkham.ui.error

import com.copperleaf.ballast.BallastViewModel

typealias NavigationErrorViewModel = BallastViewModel<
    NavigationErrorContract.Inputs,
    NavigationErrorContract.Events,
    NavigationErrorContract.State>

