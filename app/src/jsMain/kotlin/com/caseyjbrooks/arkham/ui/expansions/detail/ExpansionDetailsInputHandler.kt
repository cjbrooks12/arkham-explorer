package com.caseyjbrooks.arkham.ui.expansions.detail

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.map
import kotlinx.coroutines.flow.map

class ExpansionDetailsInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    ExpansionDetailsContract.Inputs,
    ExpansionDetailsContract.Events,
    ExpansionDetailsContract.State,
    >  {
    override suspend fun InputHandlerScope<ExpansionDetailsContract.Inputs, ExpansionDetailsContract.Events, ExpansionDetailsContract.State>.handleInput(
        input: ExpansionDetailsContract.Inputs
    ) = when (input) {
        is ExpansionDetailsContract.Inputs.Initialize -> {
            updateState { it.copy(expansionId = input.expansionId) }
            observeFlows(
                "Encounter Sets",
                repository
                    .getExpansions(false)
                    .map { cached ->
                        cached
                            .map { expansions ->
                                val expansionMatch = expansions
                                    .asSequence()
                                    .firstOrNull { expansion -> expansion.name == input.expansionId }

                                expansionMatch!!
                            }
                            .let { ExpansionDetailsContract.Inputs.ExpansionUpdated(it) }
                    }
            )
        }

        is ExpansionDetailsContract.Inputs.ExpansionUpdated -> {
            updateState { it.copy(expansion = input.expansion) }
        }
    }
}
