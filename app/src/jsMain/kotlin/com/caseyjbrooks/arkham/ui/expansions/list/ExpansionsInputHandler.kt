package com.caseyjbrooks.arkham.ui.expansions.list

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

class ExpansionsInputHandler : InputHandler<
    ExpansionsContract.Inputs,
    ExpansionsContract.Events,
    ExpansionsContract.State,
    >  {
    override suspend fun InputHandlerScope<ExpansionsContract.Inputs, ExpansionsContract.Events, ExpansionsContract.State>.handleInput(
        input: ExpansionsContract.Inputs
    ) {
        TODO("Not yet implemented")
    }
}
