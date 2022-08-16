package com.caseyjbrooks.arkham.ui.tools.campaignlog

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope

class CampaignLogViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: CampaignLogInputHandler,
) : BasicViewModel<
    CampaignLogContract.Inputs,
    CampaignLogContract.Events,
    CampaignLogContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = CampaignLogContract.State(),
            name = "Campaign Log",
        ),
    eventHandler = eventHandler { },
)
