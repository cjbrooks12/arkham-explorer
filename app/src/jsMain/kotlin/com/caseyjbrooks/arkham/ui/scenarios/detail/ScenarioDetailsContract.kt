package com.caseyjbrooks.arkham.ui.scenarios.detail

object ScenarioDetailsContract {
    data class State(
        val scenarioId: String = "",
    )

    sealed class Inputs {
        data class Initialize(val scenarioId: String) : Inputs()
    }

    sealed class Events
}
