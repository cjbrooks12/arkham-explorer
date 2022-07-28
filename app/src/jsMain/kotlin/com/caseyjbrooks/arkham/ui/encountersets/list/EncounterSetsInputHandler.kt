package com.caseyjbrooks.arkham.ui.encountersets.list

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class EncounterSetsInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    EncounterSetsContract.Inputs,
    EncounterSetsContract.Events,
    EncounterSetsContract.State,
    > {

    override suspend fun InputHandlerScope<EncounterSetsContract.Inputs, EncounterSetsContract.Events, EncounterSetsContract.State>.handleInput(
        input: EncounterSetsContract.Inputs
    ) = when (input) {
        is EncounterSetsContract.Inputs.Initialize -> {
            observeFlows(
                "Expansions",
                repository
                    .getExpansions(false)
                    .map { EncounterSetsContract.Inputs.EncounterSetsUpdated(it) }
            )
        }

        is EncounterSetsContract.Inputs.EncounterSetsUpdated -> {
            updateState { it.copy(expansions = input.expansions) }
        }
    }
}
