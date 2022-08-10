package com.caseyjbrooks.arkham.ui.expansions.detail

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.Expansion
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.ballast.repository.cache.Cached

object ExpansionDetailsContract {
    data class State(
        val expansionCode: String = "",
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val expansion: Cached<Expansion> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val expansionCode: String) : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class ExpansionUpdated(val expansion: Cached<Expansion>) : Inputs()
    }

    sealed class Events
}
