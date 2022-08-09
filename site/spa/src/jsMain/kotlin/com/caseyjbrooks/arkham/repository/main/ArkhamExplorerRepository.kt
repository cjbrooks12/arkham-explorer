package com.caseyjbrooks.arkham.repository.main

import com.copperleaf.arkham.models.ArkhamExplorerStaticPage
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

interface ArkhamExplorerRepository {
    fun getExpansions(forceRefresh: Boolean): Flow<Cached<List<ArkhamHorrorExpansion>>>
    fun getStaticPageContent(forceRefresh: Boolean, slug: String): Flow<Cached<ArkhamExplorerStaticPage>>
}
