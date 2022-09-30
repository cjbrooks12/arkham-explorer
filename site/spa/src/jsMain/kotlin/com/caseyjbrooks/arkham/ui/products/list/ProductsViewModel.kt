package com.caseyjbrooks.arkham.ui.products.list

import com.copperleaf.ballast.BallastViewModel

typealias ProductsViewModel = BallastViewModel<
    ProductsContract.Inputs,
    ProductsContract.Events,
    ProductsContract.State>

