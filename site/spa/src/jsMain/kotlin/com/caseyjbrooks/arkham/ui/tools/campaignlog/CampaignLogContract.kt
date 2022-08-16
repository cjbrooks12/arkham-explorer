package com.caseyjbrooks.arkham.ui.tools.campaignlog

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.Expansion
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.ballast.repository.cache.Cached

object CampaignLogContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val expansionCode: String? = null,
        val campaignLogId: String? = null,
        val expansion: Cached<Expansion> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val expansionCode: String?, val campaignLogId: String?) : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class ExpansionUpdated(val expansion: Cached<Expansion>) : Inputs()
    }

    sealed class Events
}
