package com.caseyjbrooks.arkham.ui.home

import com.caseyjbrooks.arkham.models.NavigationSection
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object HomeContract {
    data class State(
        val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded(),
        val startNavigation: Cached<List<NavigationSection>> = Cached.NotLoaded(),
        val endNavigation: Cached<List<NavigationSection>> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<List<ArkhamHorrorExpansion>>) : Inputs()
    }

    sealed class Events

}
