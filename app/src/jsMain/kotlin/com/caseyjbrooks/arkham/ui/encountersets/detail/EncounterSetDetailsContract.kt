package com.caseyjbrooks.arkham.ui.encountersets.detail

object EncounterSetDetailsContract {
    data class State(
        val encounterSetId: String = "",
    )

    sealed class Inputs {
        data class Initialize(val encounterSetId: String) : Inputs()
    }

    sealed class Events
}
