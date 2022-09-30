package com.caseyjbrooks.arkham.ui.scenarios.detail

import com.copperleaf.ballast.BallastViewModel

typealias ScenarioDetailsViewModel = BallastViewModel<
    ScenarioDetailsContract.Inputs,
    ScenarioDetailsContract.Events,
    ScenarioDetailsContract.State>
