package com.caseyjbrooks.arkham.ui.tools.campaignlog

import com.copperleaf.ballast.BallastViewModel

typealias CampaignLogViewModel = BallastViewModel<
    CampaignLogContract.Inputs,
    CampaignLogContract.Events,
    CampaignLogContract.State>

