package com.caseyjbrooks.arkham.ui.chaosbag

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.removeAllPreservingDuplicates
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.AutoFail
import com.copperleaf.arkham.models.api.ChaosBagDifficulty
import com.copperleaf.arkham.models.api.Cultist
import com.copperleaf.arkham.models.api.ElderSign
import com.copperleaf.arkham.models.api.NumberToken
import com.copperleaf.arkham.models.api.Skull
import com.copperleaf.arkham.models.api.Tablet
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.awaitValue
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import kotlinx.coroutines.flow.map

class ChaosBagSimulatorInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    ChaosBagSimulatorContract.Inputs,
    ChaosBagSimulatorContract.Events,
    ChaosBagSimulatorContract.State> {

    private val defaultInitialTokens = listOf(
        NumberToken(+1),
        NumberToken(+1),
        NumberToken(0),
        NumberToken(0),
        NumberToken(0),
        NumberToken(-1),
        NumberToken(-1),
        NumberToken(-1),
        NumberToken(-2),
        NumberToken(-2),
        Skull,
        Skull,
        Cultist,
        Tablet,
        AutoFail,
        ElderSign,
    )

    override suspend fun InputHandlerScope<ChaosBagSimulatorContract.Inputs, ChaosBagSimulatorContract.Events, ChaosBagSimulatorContract.State>.handleInput(
        input: ChaosBagSimulatorContract.Inputs
    ) = when (input) {
        is ChaosBagSimulatorContract.Inputs.InitializeDefault -> {
            updateState {
                it.copy(
                    scenarioId = null,
                    scenario = null,
                    selectedDifficulty = ChaosBagDifficulty.Standard,
                    tokens = defaultInitialTokens,
                    consumedTokens = emptyList(),
                )
            }

            observeFlows(
                "Home",
                repository
                    .getExpansions(false)
                    .map { ChaosBagSimulatorContract.Inputs.ExpansionsUpdated(it) }
            )
        }

        is ChaosBagSimulatorContract.Inputs.InitializeForScenario -> {
            val scenario = repository.getScenario(false, input.scenarioId).awaitValue().getCachedOrThrow()
            val initialDifficulty = if (scenario.chaosBag.isNotEmpty()) {
                if (scenario.chaosBag.any { it.difficulty == ChaosBagDifficulty.Standard }) {
                    ChaosBagDifficulty.Standard
                } else {
                    scenario.chaosBag.first().difficulty
                }
            } else {
                ChaosBagDifficulty.Standard
            }
            val initialChaosBag = scenario
                .chaosBag
                .singleOrNull { it.difficulty == initialDifficulty }
                ?.tokens
                ?: defaultInitialTokens

            updateState {
                it.copy(
                    scenarioId = input.scenarioId,
                    scenario = scenario,
                    selectedDifficulty = initialDifficulty,
                    tokens = initialChaosBag,
                    consumedTokens = emptyList(),
                )
            }

            observeFlows(
                "Home",
                repository
                    .getExpansions(false)
                    .map { ChaosBagSimulatorContract.Inputs.ExpansionsUpdated(it) }
            )
        }

        is ChaosBagSimulatorContract.Inputs.ChangeScenarioDifficulty -> {
            val currentState = getCurrentState()
            if (currentState.scenarioId == null || currentState.scenario == null) {
                noOp()
            } else {
                val newChaosBag = currentState
                    .scenario
                    .chaosBag
                    .single { it.difficulty == input.difficulty }
                    .tokens

                updateState {
                    it.copy(
                        selectedDifficulty = input.difficulty,
                        tokens = newChaosBag,
                        consumedTokens = emptyList(),
                    )
                }
            }
        }

        is ChaosBagSimulatorContract.Inputs.AddTokenToBag -> {
            updateState {
                it.copy(
                    tokens = it.tokens + input.token,
                    consumedTokens = emptyList(),
                )
            }
        }

        is ChaosBagSimulatorContract.Inputs.RemoveTokenFromBag -> {
            updateState {
                it.copy(
                    tokens = it.tokens - input.token,
                    consumedTokens = emptyList(),
                )
            }
        }

        is ChaosBagSimulatorContract.Inputs.DrawToken -> {
            updateState {
                val randomToken = it.remainingTokens.randomOrNull(it.random)
                it.copy(
                    consumedTokens = it.consumedTokens + listOfNotNull(randomToken)
                )
            }
        }

        is ChaosBagSimulatorContract.Inputs.PutTokenBack -> {
            val currentState = getCurrentState()
            check(input.token in currentState.consumedTokens) {
                "${input.token} was not drawn already. Use Inputs.AddTokenToBag to add a new token to the bag"
            }
            updateState {
                it.copy(
                    consumedTokens = it.consumedTokens.removeAllPreservingDuplicates(listOf(input.token))
                )
            }
        }

        is ChaosBagSimulatorContract.Inputs.PutAllTokensBack -> {
            updateState {
                it.copy(
                    consumedTokens = emptyList()
                )
            }
        }

        is ChaosBagSimulatorContract.Inputs.ExpansionsUpdated -> {
            updateState {
                it.copy(
                    layout = MainLayoutState.fromCached(input.expansions)
                )
            }
        }
    }
}
