package com.caseyjbrooks.arkham.ui.investigators.detail

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.InvestigatorDetails
import com.copperleaf.arkham.models.api.InvestigatorId
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.map

object InvestigatorDetailsContract {
    data class State(
        val investigatorId: InvestigatorId = InvestigatorId(""),
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val investigator: Cached<InvestigatorDetails> = Cached.NotLoaded(),
    ) {
        val parentExpansion: Cached<ExpansionLite> = layout
            .map { layoutValue ->
                layoutValue.expansions.single { expansion ->
                    expansion.expansionCode == investigator.getCachedOrNull()?.expansionCode
                }
            }
    }

    sealed class Inputs {
        data class Initialize(val investigatorId: InvestigatorId) : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class InvestigatorUpdated(val investigator: Cached<InvestigatorDetails>) : Inputs()
    }

    sealed class Events
}
