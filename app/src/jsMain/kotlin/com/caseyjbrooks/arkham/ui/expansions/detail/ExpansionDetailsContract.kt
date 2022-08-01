package com.caseyjbrooks.arkham.ui.expansions.detail

import com.caseyjbrooks.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull

object ExpansionDetailsContract {
    data class State(
        val expansionId: String = "",
        val allExpansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded(),
        val expansion: Cached<ArkhamHorrorExpansion> = Cached.NotLoaded(),
    ) {
        fun getEncounterSetByName(name: String): ArkhamHorrorExpansion.EncounterSet {
            return allExpansions
                .getCachedOrNull()
                ?.asSequence()
                ?.flatMap { it.encounterSets }
                ?.filter { it.name == name }
                ?.firstOrNull()
                ?: error("$name not found")
        }
    }

    sealed class Inputs {
        data class Initialize(val expansionId: String) : Inputs()
        data class ExpansionUpdated(
            val allExpansions: Cached<List<ArkhamHorrorExpansion>>,
            val expansion: Cached<ArkhamHorrorExpansion>
        ) : Inputs()
    }

    sealed class Events
}
