package com.caseyjbrooks.arkham.ui.investigators.detail

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
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
    >  {
    override suspend fun InputHandlerScope<InvestigatorDetailsContract.Inputs, InvestigatorDetailsContract.Events, InvestigatorDetailsContract.State>.handleInput(
        input: InvestigatorDetailsContract.Inputs
    ) = when(input) {
        is InvestigatorDetailsContract.Inputs.Initialize -> {
            updateState { it.copy(investigatorId = input.investigatorId) }
            observeFlows(
                "Encounter Sets",
                repository
                    .getExpansions(false)
                    .map { cached ->
                        cached
                            .map { expansions ->
                                val investigatorMatch = expansions
                                    .asSequence()
                                    .flatMap { expansion -> expansion.investigators.map { investigator -> expansion to investigator } }
                                    .firstOrNull { (expansion, investigator) -> investigator.name == input.investigatorId }

                                investigatorMatch!!
                            }
                            .let { InvestigatorDetailsContract.Inputs.InvestigatorUpdated(it) }
                    }
            )
        }

        is InvestigatorDetailsContract.Inputs.InvestigatorUpdated -> {
            updateState { it.copy(investigator = input.investigator) }
        }
    }
}
