package com.caseyjbrooks.arkham.ui.encountersets.list

import com.caseyjbrooks.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object EncounterSetsContract {
    data class State(
        val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded()
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class EncounterSetsUpdated(val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded()): Inputs()
    }

    sealed class Events
}
