package com.caseyjbrooks.arkham.ui.investigators.detail

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

class InvestigatorDetailsInputHandler : InputHandler<
    InvestigatorDetailsContract.Inputs,
    InvestigatorDetailsContract.Events,
    InvestigatorDetailsContract.State,
    >  {
    override suspend fun InputHandlerScope<InvestigatorDetailsContract.Inputs, InvestigatorDetailsContract.Events, InvestigatorDetailsContract.State>.handleInput(
        input: InvestigatorDetailsContract.Inputs
    ) = when(input) {
        is InvestigatorDetailsContract.Inputs.Initialize -> {
            updateState { it.copy(investigatorId = input.investigatorId) }
        }
    }
}
