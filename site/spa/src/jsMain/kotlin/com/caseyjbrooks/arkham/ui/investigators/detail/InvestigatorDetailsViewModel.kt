package com.caseyjbrooks.arkham.ui.investigators.detail

import com.copperleaf.ballast.BallastViewModel

typealias InvestigatorDetailsViewModel = BallastViewModel<
    InvestigatorDetailsContract.Inputs,
    InvestigatorDetailsContract.Events,
    InvestigatorDetailsContract.State>
