package com.caseyjbrooks.arkham.repository.main

import com.copperleaf.arkham.models.ArkhamExplorerStaticPage
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached

object ArkhamExplorerContract {
    data class State(
        val initialized: Boolean = false,

        val expansionsInitialized: Boolean = false,
        val expansions: Cached<List<ArkhamHorrorExpansion>> = Cached.NotLoaded(),

        val staticPageContentInitialized: Map<String, Boolean> = emptyMap(),
        val staticPageContent: Map<String, Cached<ArkhamExplorerStaticPage>> = emptyMap()
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class RefreshExpansions(val forceRefresh: Boolean) : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<List<ArkhamHorrorExpansion>>) : Inputs()

        data class RefreshStaticPageContent(val forceRefresh: Boolean, val slug: String) : Inputs()
        data class StaticPageContentUpdated(val slug: String, val content: Cached<ArkhamExplorerStaticPage>) : Inputs()
    }
}
