package com.caseyjbrooks.arkham.ui.home

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class HomeInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    HomeContract.Inputs,
    HomeContract.Events,
    HomeContract.State> {

    override suspend fun InputHandlerScope<HomeContract.Inputs, HomeContract.Events, HomeContract.State>.handleInput(
        input: HomeContract.Inputs
    ) = when (input) {
        is HomeContract.Inputs.Initialize -> {
            observeFlows(
                "Home",
                repository
                    .getExpansions(false)
                    .map { HomeContract.Inputs.ExpansionsUpdated(it) }
            )
        }

        is HomeContract.Inputs.ExpansionsUpdated -> {
            updateState { it.copy(expansions = input.expansions) }
        }
    }
}
