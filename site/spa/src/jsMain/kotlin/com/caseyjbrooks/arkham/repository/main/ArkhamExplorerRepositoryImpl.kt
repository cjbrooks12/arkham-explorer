package com.caseyjbrooks.arkham.repository.main

import com.copperleaf.arkham.models.ArkhamExplorerStaticPage
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArkhamExplorerRepositoryImpl(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: ArkhamExplorerInputHandler,
    eventBus: EventBus,
) : BallastRepository<
    ArkhamExplorerContract.Inputs,
    ArkhamExplorerContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            this += BootstrapInterceptor { ArkhamExplorerContract.Inputs.Initialize }
        }
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = ArkhamExplorerContract.State(),
            name = "Arkham Explorer",
        ),
    eventBus = eventBus,
), ArkhamExplorerRepository {

    override fun getExpansions(forceRefresh: Boolean): Flow<Cached<List<ArkhamHorrorExpansion>>> {
        trySend(ArkhamExplorerContract.Inputs.RefreshExpansions(forceRefresh))

        return observeStates()
            .map { it.expansions }
    }

    override fun getStaticPageContent(forceRefresh: Boolean, slug: String): Flow<Cached<ArkhamExplorerStaticPage>> {
        trySend(ArkhamExplorerContract.Inputs.RefreshStaticPageContent(forceRefresh, slug))

        return observeStates()
            .map { it.staticPageContent[slug] ?: Cached.NotLoaded() }
    }
}
