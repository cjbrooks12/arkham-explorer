package com.caseyjbrooks.arkham.ui.investigators.list

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

class InvestigatorsInputHandler : InputHandler<
    InvestigatorsContract.Inputs,
    InvestigatorsContract.Events,
    InvestigatorsContract.State,
    >  {
    override suspend fun InputHandlerScope<InvestigatorsContract.Inputs, InvestigatorsContract.Events, InvestigatorsContract.State>.handleInput(
        input: InvestigatorsContract.Inputs
    ) {
        TODO("Not yet implemented")
    }
}
