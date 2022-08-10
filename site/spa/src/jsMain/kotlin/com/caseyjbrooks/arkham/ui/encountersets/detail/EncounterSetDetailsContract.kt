package com.caseyjbrooks.arkham.ui.encountersets.detail

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.EncounterSet
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.ballast.repository.cache.Cached

object EncounterSetDetailsContract {
    data class State(
        val encounterSetId: EncounterSetId = EncounterSetId(""),
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val parentExpansion: Cached<ExpansionLite> = Cached.NotLoaded(),
        val encounterSet: Cached<EncounterSet> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val encounterSetId: EncounterSetId) : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class EncounterSetUpdated(val encounterSet: Cached<EncounterSet>) : Inputs()
    }

    sealed class Events
}
