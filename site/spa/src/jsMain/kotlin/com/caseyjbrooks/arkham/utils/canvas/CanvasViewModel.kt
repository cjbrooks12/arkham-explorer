package com.caseyjbrooks.arkham.utils.canvas

import com.copperleaf.ballast.BallastViewModel

typealias CanvasViewModel = BallastViewModel<
    CanvasContract.Inputs,
    CanvasContract.Events,
    CanvasContract.State>
