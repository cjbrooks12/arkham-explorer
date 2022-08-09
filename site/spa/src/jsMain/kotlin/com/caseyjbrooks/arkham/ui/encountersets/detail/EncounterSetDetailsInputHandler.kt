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
                "Encounter Sets",
                repository
                    .getExpansions(false)
                    .map { cached ->
                        cached
                            .map { expansions ->
                                val encounterSetMatch = expansions
                                    .asSequence()
                                    .flatMap { expansion -> expansion.encounterSets.map { encounterSet -> expansion to encounterSet } }
                                    .firstOrNull { (expansion, encounterSet) -> encounterSet.name == input.encounterSetId }

                                encounterSetMatch!!
                            }
                            .let { EncounterSetDetailsContract.Inputs.EncounterSetUpdated(cached, it) }
                    }
            )
        }

        is EncounterSetDetailsContract.Inputs.EncounterSetUpdated -> {
            updateState {
                it.copy(
                    layout = MainLayoutState.fromCached(input.expansions),
                    encounterSet = input.encounterSet,
                )
            }
        }
    }
}
