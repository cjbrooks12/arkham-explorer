package com.caseyjbrooks.arkham.ui.investigators.list

import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object InvestigatorsContract {
    data class State(
        val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded()
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class InvestigatorsUpdated(val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded()): Inputs()
    }
    sealed class Events
}
