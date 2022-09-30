package com.caseyjbrooks.arkham.ui.products.detail

import com.copperleaf.ballast.BallastViewModel

typealias ProductDetailsViewModel = BallastViewModel<
    ProductDetailsContract.Inputs,
    ProductDetailsContract.Events,
    ProductDetailsContract.State>
