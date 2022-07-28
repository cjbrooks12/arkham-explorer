package com.caseyjbrooks.arkham.repository.main

import com.caseyjbrooks.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object ArkhamExplorerContract {
    data class State(
        val initialized: Boolean = false,

        val expansionsInitialized: Boolean = false,
        val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded()
    )
//    {
//        val encounterSets: List<Pair<ArkhamHorrorExpansion.EncounterSets, List<ArkhamHorrorExpansion.Scenarios>>> =
//            expansions
//                .flatMap { expansion ->
//                    expansion.encounterSets.map { encounterSet ->
//                        encounterSet to expansions
//                            .flatMap { it.scenarios }
//                            .filter { scenario -> scenario.encounterSets.any { it.name == encounterSet.name } }
//                    }
//                }
//    }

    sealed class Inputs {
        object Initialize : Inputs()
        data class RefreshExpansions(val forceRefresh: Boolean) : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<List<ArkhamHorrorExpansion>>) : Inputs()
    }
}
