package com.caseyjbrooks.arkham.ui.scenarios.list

import com.caseyjbrooks.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object ScenariosContract {
    data class State(
        val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded()
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class ScenariosUpdated(val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded()): Inputs()
    }

    sealed class Events
}
