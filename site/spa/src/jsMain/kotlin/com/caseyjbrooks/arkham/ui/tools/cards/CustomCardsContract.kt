package com.caseyjbrooks.arkham.ui.tools.cards

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.ballast.repository.cache.Cached

object CustomCardsContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
    }

    sealed class Events
}
