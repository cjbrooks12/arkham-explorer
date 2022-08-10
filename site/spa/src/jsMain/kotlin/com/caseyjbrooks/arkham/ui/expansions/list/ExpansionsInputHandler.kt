package com.caseyjbrooks.arkham.ui.expansions.list

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class ExpansionsInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    ExpansionsContract.Inputs,
    ExpansionsContract.Events,
    ExpansionsContract.State,
    > {
    override suspend fun InputHandlerScope<ExpansionsContract.Inputs, ExpansionsContract.Events, ExpansionsContract.State>.handleInput(
        input: ExpansionsContract.Inputs
    ) = when (input) {
        is ExpansionsContract.Inputs.Initialize -> {
            observeFlows(
                "Expansions",
                repository
                    .getExpansions(false)
                    .map { ExpansionsContract.Inputs.ExpansionsUpdated(it) },
            )
        }

        is ExpansionsContract.Inputs.ExpansionsUpdated -> {
            updateState { it.copy(layout = MainLayoutState.fromCached(input.expansions)) }
        }
    }
}
