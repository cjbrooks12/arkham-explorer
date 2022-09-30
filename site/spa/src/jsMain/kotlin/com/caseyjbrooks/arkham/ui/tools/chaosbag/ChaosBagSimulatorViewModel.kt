package com.caseyjbrooks.arkham.ui.tools.chaosbag

import com.copperleaf.ballast.BallastViewModel

typealias ChaosBagSimulatorViewModel = BallastViewModel<
    ChaosBagSimulatorContract.Inputs,
    ChaosBagSimulatorContract.Events,
    ChaosBagSimulatorContract.State>
