package com.caseyjbrooks.arkham.ui.encountersets.detail

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.EncounterSetDetails
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.map

object EncounterSetDetailsContract {
    data class State(
        val encounterSetId: EncounterSetId = EncounterSetId(""),
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val encounterSet: Cached<EncounterSetDetails> = Cached.NotLoaded(),
    ) {
        val parentExpansion: Cached<ExpansionLite> = layout
            .map { layoutValue ->
                layoutValue.expansions.single { expansion ->
                    expansion.expansionCode == encounterSet.getCachedOrNull()?.expansionCode
                }
            }
    }

    sealed class Inputs {
        data class Initialize(val encounterSetId: EncounterSetId) : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class EncounterSetUpdated(val encounterSet: Cached<EncounterSetDetails>) : Inputs()
    }

    sealed class Events
}
