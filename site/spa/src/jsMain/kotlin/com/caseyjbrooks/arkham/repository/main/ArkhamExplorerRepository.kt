package com.caseyjbrooks.arkham.repository.main

import com.caseyjbrooks.arkham.utils.canvas.CanvasDefinition
import com.caseyjbrooks.arkham.utils.form.FormDefinition
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
import com.copperleaf.arkham.models.api.StaticPage
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

interface ArkhamExplorerRepository {

    fun getExpansions(forceRefresh: Boolean): Flow<Cached<ExpansionList>>
    fun getExpansion(forceRefresh: Boolean, code: String): Flow<Cached<Expansion>>

    fun getScenarios(forceRefresh: Boolean): Flow<Cached<ScenarioList>>
    fun getScenario(forceRefresh: Boolean, scenarioId: ScenarioId): Flow<Cached<ScenarioDetails>>

    fun getEncounterSets(forceRefresh: Boolean): Flow<Cached<EncounterSetList>>
    fun getEncounterSet(forceRefresh: Boolean, encounterSetId: EncounterSetId): Flow<Cached<EncounterSetDetails>>

    fun getInvestigators(forceRefresh: Boolean): Flow<Cached<InvestigatorList>>
    fun getInvestigator(forceRefresh: Boolean, investigatorId: InvestigatorId): Flow<Cached<InvestigatorDetails>>

    fun getProducts(forceRefresh: Boolean): Flow<Cached<ProductList>>
    fun getProduct(forceRefresh: Boolean, productId: ProductId): Flow<Cached<ProductDetails>>

    fun getStaticPageContent(forceRefresh: Boolean, slug: String): Flow<Cached<StaticPage>>

    fun getFormDefinition(forceRefresh: Boolean, slug: String): Flow<Cached<FormDefinition>>
    fun getCanvasDefinition(forceRefresh: Boolean, slug: String): Flow<Cached<CanvasDefinition>>
}
