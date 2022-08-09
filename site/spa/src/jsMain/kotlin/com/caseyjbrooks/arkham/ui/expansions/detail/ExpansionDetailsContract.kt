package com.caseyjbrooks.arkham.ui.expansions.detail

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull

object ExpansionDetailsContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val expansionId: String = "",
        val expansion: Cached<ArkhamHorrorExpansion> = Cached.NotLoaded(),
    ) {
        fun getEncounterSetByName(name: String): ArkhamHorrorExpansion.EncounterSet {
            return layout
                .getCachedOrNull()
                ?.expansions
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
            val expansions: Cached<List<ArkhamHorrorExpansion>>,
            val expansion: Cached<ArkhamHorrorExpansion>,
        ) : Inputs()
    }

    sealed class Events
}
