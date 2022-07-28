package com.caseyjbrooks.arkham.ui.scenarios.list

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class ScenariosInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    ScenariosContract.Inputs,
    ScenariosContract.Events,
    ScenariosContract.State,
    >  {
    override suspend fun InputHandlerScope<ScenariosContract.Inputs, ScenariosContract.Events, ScenariosContract.State>.handleInput(
        input: ScenariosContract.Inputs
    ) = when(input) {
        is ScenariosContract.Inputs.Initialize -> {
            observeFlows(
                "Scenarios",
                repository
                    .getExpansions(false)
                    .map { ScenariosContract.Inputs.ScenariosUpdated(it) }
            )
        }
        is ScenariosContract.Inputs.ScenariosUpdated -> {
            updateState { it.copy(expansions = input.expansions) }
        }
    }
}
