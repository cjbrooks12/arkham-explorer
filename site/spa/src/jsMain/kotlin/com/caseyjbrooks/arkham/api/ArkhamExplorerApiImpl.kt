package com.caseyjbrooks.arkham.api

import com.caseyjbrooks.arkham.config.ArkhamConfig
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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ArkhamExplorerApiImpl(
    private val config: ArkhamConfig,
    private val httpClient: HttpClient,
) : ArkhamExplorerApi {
    override suspend fun getExpansions(): ExpansionList {
        return httpClient
            .get("api/expansions.json")
            .body()
    }

    override suspend fun getExpansion(code: String): Expansion {
        return httpClient
            .get("api/expansions/$code.json")
            .body()
    }

    override suspend fun getScenarios(): ScenarioList {
        return httpClient
            .get("api/scenarios.json")
            .body()
    }

    override suspend fun getScenario(scenarioId: ScenarioId): Scenario {
        return httpClient
            .get("api/scenarios/${scenarioId.id}.json")
            .body()
    }

    override suspend fun getEncounterSets(): EncounterSetList {
        return httpClient
            .get("api/encounter-sets.json")
            .body()
    }

    override suspend fun getEncounterSet(encounterSetId: EncounterSetId): EncounterSet {
        return httpClient
            .get("api/encounter-sets/${encounterSetId.id}.json")
            .body()
    }

    override suspend fun getInvestigators(): InvestigatorList {
        return httpClient
            .get("api/investigators.json")
            .body()
    }

    override suspend fun getInvestigator(investigatorId: InvestigatorId): Investigator {
        return httpClient
            .get("api/investigators/${investigatorId.id}.json")
            .body()
    }

    override suspend fun getProducts(): ProductList {
        return httpClient
            .get("api/products.json")
            .body()
    }

    override suspend fun getProduct(productId: ProductId): Product {
        return httpClient
            .get("api/products/${productId.id}.json")
            .body()
    }

    override suspend fun getStaticPageContent(slug: String): ArkhamExplorerStaticPage {
        return httpClient
            .get("pages/$slug.json")
            .body()
    }
}
