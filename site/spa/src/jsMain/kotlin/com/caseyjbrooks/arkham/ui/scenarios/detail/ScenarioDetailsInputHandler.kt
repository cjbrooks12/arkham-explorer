package com.caseyjbrooks.arkham.ui.scenarios.detail

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

class ScenarioDetailsInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    ScenarioDetailsContract.Inputs,
    ScenarioDetailsContract.Events,
    ScenarioDetailsContract.State,
    > {
    override suspend fun InputHandlerScope<ScenarioDetailsContract.Inputs, ScenarioDetailsContract.Events, ScenarioDetailsContract.State>.handleInput(
        input: ScenarioDetailsContract.Inputs
    ) = when (input) {
        is ScenarioDetailsContract.Inputs.Initialize -> {
            updateState { it.copy(scenarioId = input.scenarioId) }
            observeFlows(
                "Scenario Details",
                repository
                    .getExpansions(false)
                    .map { ScenarioDetailsContract.Inputs.ExpansionsUpdated(it) },
                repository
                    .getScenario(false, input.scenarioId)
                    .map { ScenarioDetailsContract.Inputs.ScenarioUpdated(it) }
            )
        }

        is ScenarioDetailsContract.Inputs.ExpansionsUpdated -> {
            updateState { it.copy(layout = MainLayoutState.fromCached(input.expansions)) }
        }

        is ScenarioDetailsContract.Inputs.ScenarioUpdated -> {
            updateState { it.copy(scenario = input.scenario) }
        }
    }
}
