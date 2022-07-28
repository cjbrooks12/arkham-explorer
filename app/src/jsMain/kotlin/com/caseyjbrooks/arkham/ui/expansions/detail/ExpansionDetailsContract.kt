package com.caseyjbrooks.arkham.ui.expansions.detail

object ExpansionDetailsContract {
    data class State(
        val expansionId: String = "",
    )

    sealed class Inputs {
        data class Initialize(val expansionId: String) : Inputs()
    }

    sealed class Events
}
