package com.caseyjbrooks.arkham.ui.investigators.detail

object InvestigatorDetailsContract {
    data class State(
        val investigatorId: String = "",
    )

    sealed class Inputs {
        data class Initialize(val investigatorId: String) : Inputs()
    }

    sealed class Events
}
