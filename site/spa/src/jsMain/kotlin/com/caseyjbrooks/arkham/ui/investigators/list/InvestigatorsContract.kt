package com.caseyjbrooks.arkham.ui.investigators.list

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.InvestigatorList
import com.copperleaf.ballast.repository.cache.Cached

object InvestigatorsContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val investigators: Cached<InvestigatorList> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class InvestigatorsUpdated(val investigators: Cached<InvestigatorList>) : Inputs()
    }
    sealed class Events
}
