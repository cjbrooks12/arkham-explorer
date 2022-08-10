package com.caseyjbrooks.arkham.ui.scenarios.detail

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.Scenario
import com.copperleaf.arkham.models.api.ScenarioId
import com.copperleaf.ballast.repository.cache.Cached

object ScenarioDetailsContract {
    data class State(
        val scenarioId: ScenarioId = ScenarioId(""),
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val parentExpansion: Cached<ExpansionLite> = Cached.NotLoaded(),
        val scenario: Cached<Scenario> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val scenarioId: ScenarioId) : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class ScenarioUpdated(val scenario: Cached<Scenario>) : Inputs()
    }

    sealed class Events
}
