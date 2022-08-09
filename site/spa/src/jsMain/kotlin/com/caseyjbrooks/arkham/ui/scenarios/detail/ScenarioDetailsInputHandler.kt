package com.caseyjbrooks.arkham.ui.scenarios.detail

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.map
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
                "Encounter Sets",
                repository
                    .getExpansions(false)
                    .map { cached ->
                        cached
                            .map { expansions ->
                                val scenarioMatch = expansions
                                    .asSequence()
                                    .flatMap { expansion -> expansion.scenarios.map { scenario -> expansion to scenario } }
                                    .firstOrNull { (expansion, scenario) -> scenario.name == input.scenarioId }

                                scenarioMatch!!
                            }
                            .let { ScenarioDetailsContract.Inputs.ScenarioUpdated(cached, it) }
                    }
            )
        }

        is ScenarioDetailsContract.Inputs.ScenarioUpdated -> {
            updateState {
                it.copy(
                    layout = MainLayoutState.fromCached(input.expansions),
                    scenario = input.scenario,
                )
            }
        }
    }
}
