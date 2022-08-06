package com.caseyjbrooks.arkham.ui.scenarios.detail

import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object ScenarioDetailsContract {
    data class State(
        val scenarioId: String = "",
        val scenario: Cached<Pair<ArkhamHorrorExpansion, ArkhamHorrorExpansion.Scenario>> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val scenarioId: String) : Inputs()
        data class ScenarioUpdated(val scenario: Cached<Pair<ArkhamHorrorExpansion, ArkhamHorrorExpansion.Scenario>>) : Inputs()
    }

    sealed class Events
}
