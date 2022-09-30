package com.caseyjbrooks.arkham.ui.investigators.list

import com.copperleaf.ballast.BallastViewModel

typealias InvestigatorsViewModel = BallastViewModel<
    InvestigatorsContract.Inputs,
    InvestigatorsContract.Events,
    InvestigatorsContract.State>
