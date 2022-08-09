package com.caseyjbrooks.arkham.ui.scenarios.detail

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object ScenarioDetailsContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val scenarioId: String = "",
        val scenario: Cached<Pair<ArkhamHorrorExpansion, ArkhamHorrorExpansion.Scenario>> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val scenarioId: String) : Inputs()
        data class ScenarioUpdated(
            val expansions: Cached<List<ArkhamHorrorExpansion>>,
            val scenario: Cached<Pair<ArkhamHorrorExpansion, ArkhamHorrorExpansion.Scenario>>,
        ) : Inputs()
    }

    sealed class Events
}
