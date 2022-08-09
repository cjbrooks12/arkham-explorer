package com.caseyjbrooks.arkham.ui.encountersets.list

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object EncounterSetsContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class EncounterSetsUpdated(val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded()) : Inputs()
    }

    sealed class Events
}
