package com.caseyjbrooks.arkham.ui.scenarios.list

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ScenarioList
import com.copperleaf.ballast.repository.cache.Cached

object ScenariosContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val scenarios: Cached<ScenarioList> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class ScenariosUpdated(val scenarios: Cached<ScenarioList>) : Inputs()
    }

    sealed class Events
}
