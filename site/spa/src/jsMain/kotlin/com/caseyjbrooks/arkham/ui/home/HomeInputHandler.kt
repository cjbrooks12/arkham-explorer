package com.caseyjbrooks.arkham.ui.home

import com.caseyjbrooks.arkham.models.NavigationRoute
import com.caseyjbrooks.arkham.models.NavigationSection
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.map
import kotlinx.coroutines.flow.map

class HomeInputHandler(
    private val repository: ArkhamExplorerRepository,
) : InputHandler<
    HomeContract.Inputs,
    HomeContract.Events,
    HomeContract.State> {

    override suspend fun InputHandlerScope<HomeContract.Inputs, HomeContract.Events, HomeContract.State>.handleInput(
        input: HomeContract.Inputs
    ) = when (input) {
        is HomeContract.Inputs.Initialize -> {
            observeFlows(
                "Home",
                repository
                    .getExpansions(false)
                    .map { HomeContract.Inputs.ExpansionsUpdated(it) }
            )
        }

        is HomeContract.Inputs.ExpansionsUpdated -> {
            updateState {
                it.copy(
                    expansions = input.expansions,
                    startNavigation = input.expansions.map { cached ->
                        listOf(
                            NavigationSection(
                                "Expansions",
                                NavigationRoute("All Expansions", ArkhamApp.Expansions),
                                *cached
                                    .map { expansion ->
                                        NavigationRoute(expansion.name, ArkhamApp.ExpansionDetails, expansion.name)
                                    }
                                    .toTypedArray()
                            ),
                            NavigationSection(
                                "Scenarios",
                                NavigationRoute("All Scenarios", ArkhamApp.Scenarios)
                            ),
                            NavigationSection(
                                "Encounter Sets",
                                NavigationRoute("All Encounter Sets", ArkhamApp.EncounterSets)
                            ),
                            NavigationSection(
                                "Investigators",
                                NavigationRoute("All Investigators", ArkhamApp.Investigators),
                            ),
                        )
                    },
                    endNavigation = input.expansions.map { cached ->
                        listOf(
                            NavigationSection(
                                "Resources",
                                NavigationRoute("Resources", ArkhamApp.Resources),
                            ),
                        )
                    }
                )
            }
        }
    }
}
