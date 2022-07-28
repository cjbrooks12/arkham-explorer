package com.caseyjbrooks.arkham.ui.scenarios.detail

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

class ScenarioDetailsInputHandler : InputHandler<
    ScenarioDetailsContract.Inputs,
    ScenarioDetailsContract.Events,
    ScenarioDetailsContract.State,
    > {
    override suspend fun InputHandlerScope<ScenarioDetailsContract.Inputs, ScenarioDetailsContract.Events, ScenarioDetailsContract.State>.handleInput(
        input: ScenarioDetailsContract.Inputs
    ) = when (input) {
        is ScenarioDetailsContract.Inputs.Initialize -> {
            updateState { it.copy(scenarioId = input.scenarioId) }
        }
    }
}
