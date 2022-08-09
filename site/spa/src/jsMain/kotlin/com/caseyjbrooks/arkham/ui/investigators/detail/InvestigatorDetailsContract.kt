package com.caseyjbrooks.arkham.ui.investigators.detail

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object InvestigatorDetailsContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val investigatorId: String = "",
        val investigator: Cached<Pair<ArkhamHorrorExpansion, ArkhamHorrorExpansion>> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val investigatorId: String) : Inputs()
        data class InvestigatorUpdated(
            val expansions: Cached<List<ArkhamHorrorExpansion>>,
            val investigator: Cached<Pair<ArkhamHorrorExpansion, ArkhamHorrorExpansion>>,
        ) : Inputs()
    }

    sealed class Events
}
