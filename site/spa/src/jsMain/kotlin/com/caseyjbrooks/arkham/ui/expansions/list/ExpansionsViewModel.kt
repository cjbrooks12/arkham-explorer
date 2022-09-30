package com.caseyjbrooks.arkham.ui.expansions.list

import com.copperleaf.ballast.BallastViewModel

typealias ExpansionsViewModel = BallastViewModel<
    ExpansionsContract.Inputs,
    ExpansionsContract.Events,
    ExpansionsContract.State>
