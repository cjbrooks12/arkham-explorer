package com.caseyjbrooks.arkham.ui.tools.investigatortracker

import com.copperleaf.ballast.BallastViewModel

typealias InvestigatorTrackerViewModel = BallastViewModel<
    InvestigatorTrackerContract.Inputs,
    InvestigatorTrackerContract.Events,
    InvestigatorTrackerContract.State>
