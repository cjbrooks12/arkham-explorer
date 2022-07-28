package com.caseyjbrooks.arkham.ui.expansions.detail

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

class ExpansionDetailsInputHandler : InputHandler<
    ExpansionDetailsContract.Inputs,
    ExpansionDetailsContract.Events,
    ExpansionDetailsContract.State,
    >  {
    override suspend fun InputHandlerScope<ExpansionDetailsContract.Inputs, ExpansionDetailsContract.Events, ExpansionDetailsContract.State>.handleInput(
        input: ExpansionDetailsContract.Inputs
    ) = when (input) {
        is ExpansionDetailsContract.Inputs.Initialize -> {
            updateState { it.copy(expansionId = input.expansionId) }
        }
    }
}
