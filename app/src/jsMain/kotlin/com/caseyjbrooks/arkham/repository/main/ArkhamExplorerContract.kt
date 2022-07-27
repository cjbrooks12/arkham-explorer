package com.caseyjbrooks.arkham.repository.main

import com.caseyjbrooks.arkham.models.ArkhamHorrorExpansion

object ArkhamExplorerContract {
    data class State(
        val ready: Boolean = false,
        val expansions: List<ArkhamHorrorExpansion> = emptyList(),
    ) {
        val encounterSets: List<Pair<ArkhamHorrorExpansion.EncounterSets, List<ArkhamHorrorExpansion.Scenarios>>> =
            expansions
                .flatMap { expansion ->
                    expansion.encounterSets.map { encounterSet ->
                        encounterSet to expansions
                            .flatMap { it.scenarios }
                            .filter { scenario -> scenario.encounterSets.any { it.name == encounterSet.name } }
                    }
                }
    }

    sealed class Inputs {
        object Initialize : Inputs()
        data class ExpansionsLoaded(val expansions: List<ArkhamHorrorExpansion>) : Inputs()
    }

    sealed class Events {
    }
}
