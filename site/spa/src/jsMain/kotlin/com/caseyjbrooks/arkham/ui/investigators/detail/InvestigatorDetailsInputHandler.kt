package com.caseyjbrooks.arkham.ui.investigators.detail

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.map
import kotlinx.coroutines.flow.map

class InvestigatorDetailsInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    InvestigatorDetailsContract.Inputs,
    InvestigatorDetailsContract.Events,
    InvestigatorDetailsContract.State,
    > {
    override suspend fun InputHandlerScope<InvestigatorDetailsContract.Inputs, InvestigatorDetailsContract.Events, InvestigatorDetailsContract.State>.handleInput(
        input: InvestigatorDetailsContract.Inputs
    ) = when (input) {
        is InvestigatorDetailsContract.Inputs.Initialize -> {
            updateState { it.copy(investigatorId = input.investigatorId) }
            observeFlows(
                "Investigator Details",
                repository
                    .getExpansions(false)
                    .map { InvestigatorDetailsContract.Inputs.ExpansionsUpdated(it) },
                repository
                    .getInvestigator(false, input.investigatorId)
                    .map { InvestigatorDetailsContract.Inputs.InvestigatorUpdated(it) }
            )
        }

        is InvestigatorDetailsContract.Inputs.ExpansionsUpdated -> {
            updateState { state ->
                state.copy(
                    layout = MainLayoutState.fromCached(input.expansions),
                    parentExpansion = input.expansions.map { expansions ->
                        expansions.expansions.single { expansion ->
                            expansion.investigators.any { it == state.investigatorId }
                        }
                    }
                )
            }
        }

        is InvestigatorDetailsContract.Inputs.InvestigatorUpdated -> {
            updateState { it.copy(investigator = input.investigator) }
        }
    }
}
