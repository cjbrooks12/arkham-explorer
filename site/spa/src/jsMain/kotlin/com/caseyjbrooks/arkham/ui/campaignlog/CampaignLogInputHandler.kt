package com.caseyjbrooks.arkham.ui.campaignlog

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CampaignLogInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    CampaignLogContract.Inputs,
    CampaignLogContract.Events,
    CampaignLogContract.State> {

    override suspend fun InputHandlerScope<CampaignLogContract.Inputs, CampaignLogContract.Events, CampaignLogContract.State>.handleInput(
        input: CampaignLogContract.Inputs
    ) = when (input) {
        is CampaignLogContract.Inputs.Initialize -> {
            updateState { it.copy(expansionCode = input.expansionCode, campaignLogId = input.campaignLogId) }

            observeFlows(
                "Campaign Log",
                *buildList<Flow<CampaignLogContract.Inputs>> {
                    this += repository
                        .getExpansions(false)
                        .map { CampaignLogContract.Inputs.ExpansionsUpdated(it) }

                    if (input.expansionCode != null) {
                        this += repository
                            .getExpansion(false, input.expansionCode)
                            .map { CampaignLogContract.Inputs.ExpansionUpdated(it) }
                    }
                }.toTypedArray()
            )
        }

        is CampaignLogContract.Inputs.ExpansionsUpdated -> {
            updateState { it.copy(layout = MainLayoutState.fromCached(input.expansions)) }
        }

        is CampaignLogContract.Inputs.ExpansionUpdated -> {
            updateState { it.copy(expansion = input.expansion) }

        }
    }
}
