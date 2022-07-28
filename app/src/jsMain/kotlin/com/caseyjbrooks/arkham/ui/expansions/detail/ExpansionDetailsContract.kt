package com.caseyjbrooks.arkham.ui.expansions.detail

import com.caseyjbrooks.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object ExpansionDetailsContract {
    data class State(
        val expansionId: String = "",
        val expansion: Cached<ArkhamHorrorExpansion> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val expansionId: String) : Inputs()
        data class ExpansionUpdated(val expansion: Cached<ArkhamHorrorExpansion>) : Inputs()
    }

    sealed class Events
}
