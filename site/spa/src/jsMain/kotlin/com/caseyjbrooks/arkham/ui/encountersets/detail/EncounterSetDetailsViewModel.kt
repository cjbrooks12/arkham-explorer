package com.caseyjbrooks.arkham.ui.encountersets.detail

import com.copperleaf.ballast.BallastViewModel

typealias EncounterSetDetailsViewModel = BallastViewModel<
    EncounterSetDetailsContract.Inputs,
    EncounterSetDetailsContract.Events,
    EncounterSetDetailsContract.State>
