package com.caseyjbrooks.arkham.ui.encountersets.detail

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.map
import kotlinx.coroutines.flow.map

class EncounterSetDetailsInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    EncounterSetDetailsContract.Inputs,
    EncounterSetDetailsContract.Events,
    EncounterSetDetailsContract.State,
    > {
    override suspend fun InputHandlerScope<EncounterSetDetailsContract.Inputs, EncounterSetDetailsContract.Events, EncounterSetDetailsContract.State>.handleInput(
        input: EncounterSetDetailsContract.Inputs
    ) = when (input) {
        is EncounterSetDetailsContract.Inputs.Initialize -> {
            updateState { it.copy(encounterSetId = input.encounterSetId) }
            observeFlows(
                "Encounter Set Details",
                repository
                    .getExpansions(false)
                    .map { EncounterSetDetailsContract.Inputs.ExpansionsUpdated(it) },
                repository
                    .getEncounterSet(false, input.encounterSetId)
                    .map { EncounterSetDetailsContract.Inputs.EncounterSetUpdated(it) }
            )
        }

        is EncounterSetDetailsContract.Inputs.ExpansionsUpdated -> {
            updateState { state ->
                state.copy(
                    layout = MainLayoutState.fromCached(input.expansions),
                    parentExpansion = input.expansions.map { expansions ->
                        expansions.expansions.single { expansion ->
                            expansion.encounterSets.any { it == state.encounterSetId }
                        }
                    }
                )
            }
        }

        is EncounterSetDetailsContract.Inputs.EncounterSetUpdated -> {
            updateState { it.copy(encounterSet = input.encounterSet) }
        }
    }
}
