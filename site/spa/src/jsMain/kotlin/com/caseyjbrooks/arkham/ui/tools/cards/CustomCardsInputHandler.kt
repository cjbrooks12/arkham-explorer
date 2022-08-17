package com.caseyjbrooks.arkham.ui.tools.cards

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class CustomCardsInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    CustomCardsContract.Inputs,
    CustomCardsContract.Events,
    CustomCardsContract.State> {

    override suspend fun InputHandlerScope<CustomCardsContract.Inputs, CustomCardsContract.Events, CustomCardsContract.State>.handleInput(
        input: CustomCardsContract.Inputs
    ) = when (input) {
        is CustomCardsContract.Inputs.Initialize -> {
            observeFlows(
                "Home",
                repository
                    .getExpansions(false)
                    .map { CustomCardsContract.Inputs.ExpansionsUpdated(it) }
            )
        }

        is CustomCardsContract.Inputs.ExpansionsUpdated -> {
            updateState {
                it.copy(
                    layout = MainLayoutState.fromCached(input.expansions)
                )
            }
        }
    }
}
