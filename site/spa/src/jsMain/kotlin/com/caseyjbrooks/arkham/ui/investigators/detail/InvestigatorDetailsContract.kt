package com.caseyjbrooks.arkham.ui.investigators.detail

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.Investigator
import com.copperleaf.arkham.models.api.InvestigatorId
import com.copperleaf.ballast.repository.cache.Cached

object InvestigatorDetailsContract {
    data class State(
        val investigatorId: InvestigatorId = InvestigatorId(""),
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val parentExpansion: Cached<ExpansionLite> = Cached.NotLoaded(),
        val investigator: Cached<Investigator> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val investigatorId: InvestigatorId) : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class InvestigatorUpdated(val investigator: Cached<Investigator>) : Inputs()
    }

    sealed class Events
}
