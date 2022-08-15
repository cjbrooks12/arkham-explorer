package com.caseyjbrooks.arkham.repository.main

import com.caseyjbrooks.arkham.api.ArkhamExplorerApi
import com.copperleaf.arkham.models.ArkhamExplorerStaticPage
import com.copperleaf.arkham.models.api.EncounterSetDetails
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.EncounterSetList
import com.copperleaf.arkham.models.api.Expansion
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.InvestigatorDetails
import com.copperleaf.arkham.models.api.InvestigatorId
import com.copperleaf.arkham.models.api.InvestigatorList
import com.copperleaf.arkham.models.api.ProductDetails
import com.copperleaf.arkham.models.api.ProductId
import com.copperleaf.arkham.models.api.ProductList
import com.copperleaf.arkham.models.api.ScenarioDetails
import com.copperleaf.arkham.models.api.ScenarioId
import com.copperleaf.arkham.models.api.ScenarioList
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.withRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest

class ArkhamExplorerRepositoryImpl(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: ArkhamExplorerInputHandler,
    eventBus: EventBus,
    private val api: ArkhamExplorerApi,
) : BallastRepository<
    ArkhamExplorerContract.Inputs,
    ArkhamExplorerContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = ArkhamExplorerContract.State(),
            name = "Arkham Explorer",
        ),
    eventBus = eventBus,
), ArkhamExplorerRepository {

    override fun getExpansions(forceRefresh: Boolean): Flow<Cached<ExpansionList>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("ExpansionList", null)
        ) { api.getExpansions() }
    }

    override fun getExpansion(forceRefresh: Boolean, code: String): Flow<Cached<Expansion>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("ExpansionList", code)
        ) { api.getExpansion(code) }
    }

    override fun getScenarios(forceRefresh: Boolean): Flow<Cached<ScenarioList>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("ScenarioList", null)
        ) { api.getScenarios() }
    }

    override fun getScenario(forceRefresh: Boolean, scenarioId: ScenarioId): Flow<Cached<ScenarioDetails>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("ScenarioList", scenarioId.id)
        ) { api.getScenario(scenarioId) }
    }

    override fun getEncounterSets(forceRefresh: Boolean): Flow<Cached<EncounterSetList>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("EncounterSetList", null)
        ) { api.getEncounterSets() }
    }

    override fun getEncounterSet(forceRefresh: Boolean, encounterSetId: EncounterSetId): Flow<Cached<EncounterSetDetails>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("EncounterSetList", encounterSetId.id)
        ) { api.getEncounterSet(encounterSetId) }
    }

    override fun getInvestigators(forceRefresh: Boolean): Flow<Cached<InvestigatorList>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("InvestigatorList", null)
        ) { api.getInvestigators() }
    }

    override fun getInvestigator(forceRefresh: Boolean, investigatorId: InvestigatorId): Flow<Cached<InvestigatorDetails>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("InvestigatorList", investigatorId.id)
        ) { api.getInvestigator(investigatorId) }
    }

    override fun getProducts(forceRefresh: Boolean): Flow<Cached<ProductList>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("ProductList", null)
        ) { api.getProducts() }
    }

    override fun getProduct(forceRefresh: Boolean, productId: ProductId): Flow<Cached<ProductDetails>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("ProductList", productId.id)
        ) { api.getProduct(productId) }
    }

    override fun getStaticPageContent(forceRefresh: Boolean, slug: String): Flow<Cached<ArkhamExplorerStaticPage>> {
        return flowOfKey(
            forceRefresh,
            SimpleCachedValue.Key("StaticPage", slug)
        ) { api.getStaticPageContent(slug) }
    }

    private fun <T : Any> flowOfKey(
        forceRefresh: Boolean,
        key: SimpleCachedValue.Key<T>,
        doFetch: suspend () -> T
    ): Flow<Cached<T>> {
        trySend(ArkhamExplorerContract.Inputs.FetchCachedValue(forceRefresh, key, doFetch))

        return observeStates()
            .flatMapLatest {
                it.caches.singleOrNull { it.key == key }
                    ?.asFlow()
                    ?: emptyFlow()
            }
    }

}
