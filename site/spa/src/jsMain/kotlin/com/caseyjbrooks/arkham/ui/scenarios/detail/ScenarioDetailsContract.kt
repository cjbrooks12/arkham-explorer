package com.caseyjbrooks.arkham.ui.scenarios.detail

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.ScenarioDetails
import com.copperleaf.arkham.models.api.ScenarioId
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.map

object ScenarioDetailsContract {
    data class State(
        val scenarioId: ScenarioId = ScenarioId(""),
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val scenario: Cached<ScenarioDetails> = Cached.NotLoaded(),
    ) {
        val parentExpansion: Cached<ExpansionLite> = layout
            .map { layoutValue ->
                layoutValue.expansions.single { expansion ->
                    expansion.expansionCode == scenario.getCachedOrNull()?.expansionCode
                }
            }
    }

    sealed class Inputs {
        data class Initialize(val scenarioId: ScenarioId) : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class ScenarioUpdated(val scenario: Cached<ScenarioDetails>) : Inputs()
    }

    sealed class Events
}
