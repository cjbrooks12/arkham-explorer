package com.caseyjbrooks.arkham.ui.tools.cards

import com.copperleaf.ballast.BallastViewModel

typealias CustomCardsViewModel = BallastViewModel<
    CustomCardsContract.Inputs,
    CustomCardsContract.Events,
    CustomCardsContract.State>
