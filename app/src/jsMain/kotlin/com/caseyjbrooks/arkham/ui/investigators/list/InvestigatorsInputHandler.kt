package com.caseyjbrooks.arkham.ui.investigators.list

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class InvestigatorsInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    InvestigatorsContract.Inputs,
    InvestigatorsContract.Events,
    InvestigatorsContract.State,
    > {
    override suspend fun InputHandlerScope<InvestigatorsContract.Inputs, InvestigatorsContract.Events, InvestigatorsContract.State>.handleInput(
        input: InvestigatorsContract.Inputs
    ) = when (input) {
        is InvestigatorsContract.Inputs.Initialize -> {
            observeFlows(
                "Investigators",
                repository
                    .getExpansions(false)
                    .map { InvestigatorsContract.Inputs.InvestigatorsUpdated(it) }
            )
        }

        is InvestigatorsContract.Inputs.InvestigatorsUpdated -> {
            updateState { it.copy(expansions = input.expansions) }
        }
    }
}
