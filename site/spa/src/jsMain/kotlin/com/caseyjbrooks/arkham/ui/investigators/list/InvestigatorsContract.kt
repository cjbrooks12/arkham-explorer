package com.caseyjbrooks.arkham.ui.investigators.list

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object InvestigatorsContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class InvestigatorsUpdated(val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded()): Inputs()
    }
    sealed class Events
}
