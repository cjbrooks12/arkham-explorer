package com.caseyjbrooks.arkham.ui.expansions.detail

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class ExpansionDetailsInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    ExpansionDetailsContract.Inputs,
    ExpansionDetailsContract.Events,
    ExpansionDetailsContract.State,
    > {
    override suspend fun InputHandlerScope<ExpansionDetailsContract.Inputs, ExpansionDetailsContract.Events, ExpansionDetailsContract.State>.handleInput(
        input: ExpansionDetailsContract.Inputs
    ) = when (input) {
        is ExpansionDetailsContract.Inputs.Initialize -> {
            updateState { it.copy(expansionCode = input.expansionCode) }
            observeFlows(
                "Expansion Details",
                repository
                    .getExpansions(false)
                    .map { ExpansionDetailsContract.Inputs.ExpansionsUpdated(it) },
                repository
                    .getExpansion(false, input.expansionCode)
                    .map { ExpansionDetailsContract.Inputs.ExpansionUpdated(it) }
            )
        }

        is ExpansionDetailsContract.Inputs.ExpansionsUpdated -> {
            updateState {
                it.copy(
                    layout = MainLayoutState.fromCached(input.expansions),
                )
            }
        }

        is ExpansionDetailsContract.Inputs.ExpansionUpdated -> {
            updateState {
                it.copy(
                    expansion = input.expansion,
                )
            }
        }
    }
}
