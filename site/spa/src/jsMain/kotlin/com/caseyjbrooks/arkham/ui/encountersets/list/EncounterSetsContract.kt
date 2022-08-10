package com.caseyjbrooks.arkham.ui.encountersets.list

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.EncounterSetList
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.ballast.repository.cache.Cached

object EncounterSetsContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val encounterSets: Cached<EncounterSetList> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class EncounterSetsUpdated(val encounterSets: Cached<EncounterSetList> = Cached.NotLoaded()) : Inputs()
    }

    sealed class Events
}
