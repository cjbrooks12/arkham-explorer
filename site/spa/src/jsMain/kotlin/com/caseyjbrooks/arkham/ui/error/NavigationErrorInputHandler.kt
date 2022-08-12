package com.caseyjbrooks.arkham.ui.error

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class NavigationErrorInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    NavigationErrorContract.Inputs,
    NavigationErrorContract.Events,
    NavigationErrorContract.State> {

    override suspend fun InputHandlerScope<NavigationErrorContract.Inputs, NavigationErrorContract.Events, NavigationErrorContract.State>.handleInput(
        input: NavigationErrorContract.Inputs
    ) = when (input) {
        is NavigationErrorContract.Inputs.Initialize -> {
            observeFlows(
                "Home",
                repository
                    .getExpansions(false)
                    .map { NavigationErrorContract.Inputs.ExpansionsUpdated(it) }
            )
        }

        is NavigationErrorContract.Inputs.ExpansionsUpdated -> {
            updateState {
                it.copy(
                    layout = MainLayoutState.fromCached(input.expansions)
                )
            }
        }
    }
}
