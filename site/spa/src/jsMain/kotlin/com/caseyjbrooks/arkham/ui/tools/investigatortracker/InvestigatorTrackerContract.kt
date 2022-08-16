package com.caseyjbrooks.arkham.ui.tools.investigatortracker

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.InvestigatorDetails
import com.copperleaf.arkham.models.api.InvestigatorId
import com.copperleaf.ballast.repository.cache.Cached

object InvestigatorTrackerContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),

        val investigatorId: InvestigatorId? = null,
        val investigator: InvestigatorDetails? = null,

        val health: Int = 7,
        val damage: Int = 0,
        val sanity: Int = 7,
        val horror: Int = 0,
        val resources: Int = 5,
        val clues: Int = 0,
    ) {
        val healthRemaining: Int = health - damage
        val sanityRemaining: Int = sanity - horror
    }

    sealed class Inputs {
        object InitializeDefault : Inputs()
        data class InitializeForInvestigator(val investigatorId: InvestigatorId) : Inputs()

        data class UpdateHealth(val amount: Int) : Inputs()
        data class UpdateDamage(val amount: Int) : Inputs()
        data class UpdateSanity(val amount: Int) : Inputs()
        data class UpdateHorror(val amount: Int) : Inputs()
        data class UpdateResources(val amount: Int) : Inputs()
        data class UpdateClues(val amount: Int) : Inputs()

        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
    }

    sealed class Events
}
