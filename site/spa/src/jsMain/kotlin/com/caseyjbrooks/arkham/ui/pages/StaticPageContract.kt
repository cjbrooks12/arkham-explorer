package com.caseyjbrooks.arkham.ui.pages

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.ArkhamExplorerStaticPage
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object StaticPageContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val slug: String = "",
        val content: Cached<ArkhamExplorerStaticPage> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val slug: String) : Inputs()
        data class ExpansionsLoaded(val expansions: Cached<List<ArkhamHorrorExpansion>>) : Inputs()
        data class StaticPageContentLoaded(val content: Cached<ArkhamExplorerStaticPage>) : Inputs()
    }

    sealed class Events
}
