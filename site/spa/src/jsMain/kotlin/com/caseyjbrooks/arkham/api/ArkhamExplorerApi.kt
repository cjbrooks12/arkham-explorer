package com.caseyjbrooks.arkham.api

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

interface ArkhamExplorerApi {

    suspend fun getExpansions(): ExpansionList
    suspend fun getExpansion(code: String): Expansion

    suspend fun getScenarios(): ScenarioList
    suspend fun getScenario(scenarioId: ScenarioId): ScenarioDetails

    suspend fun getEncounterSets(): EncounterSetList
    suspend fun getEncounterSet(encounterSetId: EncounterSetId): EncounterSetDetails

    suspend fun getInvestigators(): InvestigatorList
    suspend fun getInvestigator(investigatorId: InvestigatorId): InvestigatorDetails

    suspend fun getProducts(): ProductList
    suspend fun getProduct(productId: ProductId): ProductDetails

    suspend fun getStaticPageContent(slug: String): StaticPage

    suspend fun getFormSchema(slug: String): FormDefinition
}
