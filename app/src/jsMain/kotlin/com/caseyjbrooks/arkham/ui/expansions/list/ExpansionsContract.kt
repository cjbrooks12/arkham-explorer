package com.caseyjbrooks.arkham.ui.expansions.list

import com.caseyjbrooks.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object ExpansionsContract {
    data class State(
        val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded()
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded()) : Inputs()
    }

    sealed class Events
}
