package com.caseyjbrooks.arkham.repository.main

import com.copperleaf.arkham.models.ArkhamExplorerStaticPage
import com.copperleaf.arkham.models.api.EncounterSet
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.EncounterSetList
import com.copperleaf.arkham.models.api.Expansion
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.Investigator
import com.copperleaf.arkham.models.api.InvestigatorId
import com.copperleaf.arkham.models.api.InvestigatorList
import com.copperleaf.arkham.models.api.Product
import com.copperleaf.arkham.models.api.ProductId
import com.copperleaf.arkham.models.api.ProductList
import com.copperleaf.arkham.models.api.Scenario
import com.copperleaf.arkham.models.api.ScenarioId
import com.copperleaf.arkham.models.api.ScenarioList
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

interface ArkhamExplorerRepository {

    fun getExpansions(forceRefresh: Boolean): Flow<Cached<ExpansionList>>
    fun getExpansion(forceRefresh: Boolean, code: String): Flow<Cached<Expansion>>

    fun getScenarios(forceRefresh: Boolean): Flow<Cached<ScenarioList>>
    fun getScenario(forceRefresh: Boolean, scenarioId: ScenarioId): Flow<Cached<Scenario>>

    fun getEncounterSets(forceRefresh: Boolean): Flow<Cached<EncounterSetList>>
    fun getEncounterSet(forceRefresh: Boolean, encounterSetId: EncounterSetId): Flow<Cached<EncounterSet>>

    fun getInvestigators(forceRefresh: Boolean): Flow<Cached<InvestigatorList>>
    fun getInvestigator(forceRefresh: Boolean, investigatorId: InvestigatorId): Flow<Cached<Investigator>>

    fun getProducts(forceRefresh: Boolean): Flow<Cached<ProductList>>
    fun getProduct(forceRefresh: Boolean, productId: ProductId): Flow<Cached<Product>>

    fun getStaticPageContent(forceRefresh: Boolean, slug: String): Flow<Cached<ArkhamExplorerStaticPage>>
}
