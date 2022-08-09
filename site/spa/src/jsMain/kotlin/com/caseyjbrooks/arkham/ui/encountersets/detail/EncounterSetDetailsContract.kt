package com.caseyjbrooks.arkham.ui.encountersets.detail

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object EncounterSetDetailsContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val encounterSetId: String = "",
        val encounterSet: Cached<Pair<ArkhamHorrorExpansion, ArkhamHorrorExpansion.EncounterSet>> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val encounterSetId: String) : Inputs()
        data class EncounterSetUpdated(
            val expansions: Cached<List<ArkhamHorrorExpansion>>,
            val encounterSet: Cached<Pair<ArkhamHorrorExpansion, ArkhamHorrorExpansion.EncounterSet>>
        ) : Inputs()
    }

    sealed class Events
}
