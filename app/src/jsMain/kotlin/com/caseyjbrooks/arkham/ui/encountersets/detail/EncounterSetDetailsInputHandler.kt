package com.caseyjbrooks.arkham.ui.encountersets.detail

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

class EncounterSetDetailsInputHandler : InputHandler<
    EncounterSetDetailsContract.Inputs,
    EncounterSetDetailsContract.Events,
    EncounterSetDetailsContract.State,
    > {
    override suspend fun InputHandlerScope<EncounterSetDetailsContract.Inputs, EncounterSetDetailsContract.Events, EncounterSetDetailsContract.State>.handleInput(
        input: EncounterSetDetailsContract.Inputs
    ) = when (input) {
        is EncounterSetDetailsContract.Inputs.Initialize -> {
            updateState { it.copy(encounterSetId = input.encounterSetId) }
        }
    }
}
