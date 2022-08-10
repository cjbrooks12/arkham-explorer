package com.caseyjbrooks.arkham.api

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

interface ArkhamExplorerApi {

    suspend fun getExpansions(): ExpansionList
    suspend fun getExpansion(code: String): Expansion

    suspend fun getScenarios(): ScenarioList
    suspend fun getScenario(scenarioId: ScenarioId): Scenario

    suspend fun getEncounterSets(): EncounterSetList
    suspend fun getEncounterSet(encounterSetId: EncounterSetId): EncounterSet

    suspend fun getInvestigators(): InvestigatorList
    suspend fun getInvestigator(investigatorId: InvestigatorId): Investigator

    suspend fun getProducts(): ProductList
    suspend fun getProduct(productId: ProductId): Product

    suspend fun getStaticPageContent(slug: String): ArkhamExplorerStaticPage
}
