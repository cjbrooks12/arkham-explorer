package com.caseyjbrooks.arkham.ui.pages

import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.StaticPage
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.ballast.repository.cache.Cached

object StaticPageContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val slug: String = "",
        val content: Cached<StaticPage> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val slug: String) : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class StaticPageContentUpdated(val content: Cached<StaticPage>) : Inputs()
    }

    sealed class Events
}
