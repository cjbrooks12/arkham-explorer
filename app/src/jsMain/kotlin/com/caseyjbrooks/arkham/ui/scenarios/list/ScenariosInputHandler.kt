package com.caseyjbrooks.arkham.ui.scenarios.list

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

class ScenariosInputHandler : InputHandler<
    ScenariosContract.Inputs,
    ScenariosContract.Events,
    ScenariosContract.State,
    >  {
    override suspend fun InputHandlerScope<ScenariosContract.Inputs, ScenariosContract.Events, ScenariosContract.State>.handleInput(
        input: ScenariosContract.Inputs
    ) {
        TODO("Not yet implemented")
    }
}
