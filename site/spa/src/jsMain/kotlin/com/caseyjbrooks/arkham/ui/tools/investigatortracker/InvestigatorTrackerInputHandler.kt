package com.caseyjbrooks.arkham.ui.tools.investigatortracker

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.awaitValue
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import kotlinx.coroutines.flow.map

class InvestigatorTrackerInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    InvestigatorTrackerContract.Inputs,
    InvestigatorTrackerContract.Events,
    InvestigatorTrackerContract.State> {

    override suspend fun InputHandlerScope<InvestigatorTrackerContract.Inputs, InvestigatorTrackerContract.Events, InvestigatorTrackerContract.State>.handleInput(
        input: InvestigatorTrackerContract.Inputs
    ) = when (input) {
        is InvestigatorTrackerContract.Inputs.InitializeDefault -> {
            updateState {
                it.copy(
                    investigatorId = null,
                    investigator = null,

                    health = 7,
                    damage = 0,
                    sanity = 7,
                    horror = 0,
                    resources = 5,
                    clues = 0,
                )
            }

            observeFlows(
                "Home",
                repository
                    .getExpansions(false)
                    .map { InvestigatorTrackerContract.Inputs.ExpansionsUpdated(it) }
            )
        }

        is InvestigatorTrackerContract.Inputs.InitializeForInvestigator -> {
            val investigator = repository.getInvestigator(false, input.investigatorId).awaitValue().getCachedOrThrow()

            updateState {
                it.copy(
                    investigatorId = input.investigatorId,
                    investigator = investigator,

                    health = investigator.health,
                    damage = 0,
                    sanity = investigator.sanity,
                    horror = 0,
                    resources = 5,
                    clues = 0,
                )
            }

            observeFlows(
                "Home",
                repository
                    .getExpansions(false)
                    .map { InvestigatorTrackerContract.Inputs.ExpansionsUpdated(it) }
            )
        }

        is InvestigatorTrackerContract.Inputs.UpdateHealth -> {
            updateState { it.copy(health = it.health + input.amount) }
        }

        is InvestigatorTrackerContract.Inputs.UpdateDamage -> {
            updateState { it.copy(damage = it.damage + input.amount) }
        }

        is InvestigatorTrackerContract.Inputs.UpdateSanity -> {
            updateState { it.copy(sanity = it.sanity + input.amount) }
        }

        is InvestigatorTrackerContract.Inputs.UpdateHorror -> {
            updateState { it.copy(horror = it.horror + input.amount) }
        }

        is InvestigatorTrackerContract.Inputs.UpdateResources -> {
            updateState { it.copy(resources = it.resources + input.amount) }
        }

        is InvestigatorTrackerContract.Inputs.UpdateClues -> {
            updateState { it.copy(clues = it.clues + input.amount) }
        }

        is InvestigatorTrackerContract.Inputs.ExpansionsUpdated -> {
            updateState {
                it.copy(
                    layout = MainLayoutState.fromCached(input.expansions)
                )
            }
        }


    }
}
