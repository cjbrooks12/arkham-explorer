package com.caseyjbrooks.arkham.ui.tools.campaignlog

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.postInput
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.ballast.repository.cache.isLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonNull

class CampaignLogInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    CampaignLogContract.Inputs,
    CampaignLogContract.Events,
    CampaignLogContract.State> {

    override suspend fun InputHandlerScope<CampaignLogContract.Inputs, CampaignLogContract.Events, CampaignLogContract.State>.handleInput(
        input: CampaignLogContract.Inputs
    ) = when (input) {
        is CampaignLogContract.Inputs.Initialize -> {
            updateState { it.copy(expansionCode = input.expansionCode, campaignLogId = input.campaignLogId) }

            observeFlows(
                "Campaign Log",
                *buildList<Flow<CampaignLogContract.Inputs>> {
                    this += repository
                        .getExpansions(false)
                        .map { CampaignLogContract.Inputs.ExpansionsUpdated(it) }

                    this += repository
                        .getFormDefinition(false, "investigators")
                        .map { CampaignLogContract.Inputs.InvestigatorsFormUpdated(it) }

                    if (input.expansionCode != null) {
                        this += repository
                            .getExpansion(false, input.expansionCode)
                            .map { CampaignLogContract.Inputs.ExpansionUpdated(it) }
                    }

                    if (input.expansionCode != "standalone") {
                        this += repository
                            .getExpansion(false, "standalone")
                            .map { CampaignLogContract.Inputs.StandaloneExpansionsUpdated(it) }
                    }
                }.toTypedArray()
            )
        }

        is CampaignLogContract.Inputs.ExpansionsUpdated -> {
            updateState { it.copy(layout = MainLayoutState.fromCached(input.expansions)) }
        }

        is CampaignLogContract.Inputs.ExpansionUpdated -> {
            val currentState = updateStateAndGet {
                it.copy(expansion = input.expansion)
            }

            if (currentState.scenarios.isEmpty() && currentState.expansionCode != null && !input.expansion.isLoading()) {
                // just now loading this scenario, automatically add the start scenario
                val expansionDetails = input.expansion.getCachedOrThrow()
                if (expansionDetails.startScenario.size == 1) {
                    // there's a single start scenario, no choice to be made. Automatically add it
                    postInput(
                        CampaignLogContract.Inputs.AddScenario(
                            expansionDetails.scenarios.single { it.id == expansionDetails.startScenario.single() }
                        )
                    )
                }
            }

            Unit
        }

        is CampaignLogContract.Inputs.StandaloneExpansionsUpdated -> {
            updateState { it.copy(standaloneExpansions = input.expansion) }
        }

        is CampaignLogContract.Inputs.InvestigatorsFormUpdated -> {
            updateState {
                it.copy(investigatorsFormDefinition = input.formDefinition)
            }
        }

        is CampaignLogContract.Inputs.ScenarioDetailsUpdated -> {
            updateState {
                it.copy(
                    scenarios = buildList {
                        it.scenarios.forEach { (scenarioLite, currentScenarioDetails) ->
                            if (scenarioLite.id == input.scenarioId) {
                                add(scenarioLite to input.scenarioDetails)
                            } else {
                                add(scenarioLite to currentScenarioDetails)
                            }
                        }
                    }
                )
            }
        }

        is CampaignLogContract.Inputs.InvestigatorsFormDataUpdated -> {
            if (input.formData == JsonNull) {
                noOp()
            } else {
                updateState { it.copy(investigatorFormData = input.formData) }
            }
        }

        is CampaignLogContract.Inputs.ScenarioFormDataUpdated -> {
            if (input.formData == JsonNull) {
                noOp()
            } else {
                updateState { it.copy(scenarioFormData = input.formData) }
            }
        }

        is CampaignLogContract.Inputs.ChangeSelectedTab -> {
            updateState { it.copy(currentScenarioId = input.scenarioId) }
        }

        is CampaignLogContract.Inputs.AddScenario -> {
            val currentState = updateStateAndGet {
                it.copy(
                    currentScenarioId = input.scenario.id,
                    scenarios = it.scenarios + (
                        input.scenario to Cached.NotLoaded()
                        )
                )
            }

            observeFlows(
                input.scenario.id.id,
                *buildList<Flow<CampaignLogContract.Inputs>> {
                    if (currentState.scenarios.size == 1) {
                        // load the initial scenario's full expansion to get the rest of the scenarios available in
                        // the campaign
                        this += repository
                            .getExpansion(false, input.scenario.expansionCode)
                            .map { CampaignLogContract.Inputs.ExpansionUpdated(it) }
                    }

                    this += repository
                        .getScenario(false, input.scenario.id)
                        .map { CampaignLogContract.Inputs.ScenarioDetailsUpdated(input.scenario.id, it) }
                }.toTypedArray()
            )
        }

        is CampaignLogContract.Inputs.RemoveScenario -> {
            updateState {
                it.copy(
                    scenarios = it.scenarios.filterNot { it.first.id == input.scenarioId }
                )
            }

            sideJob(input.scenarioId.id) {
                // do nothing, but it will kill the sideJob previously observing scenario details from the repository
            }
        }
    }
}
