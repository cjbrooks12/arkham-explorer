package com.caseyjbrooks.arkham.api

import com.caseyjbrooks.arkham.config.ArkhamConfig
import com.caseyjbrooks.arkham.models.ArkhamHorrorExpansion
import com.caseyjbrooks.arkham.models.ArkhamHorrorExpansionsIndex
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ArkhamExplorerApiImpl(
    private val config: ArkhamConfig,
    private val httpClient: HttpClient,
) : ArkhamExplorerApi {
    override suspend fun getExpansions(): List<ArkhamHorrorExpansion> {
        val expansions: ArkhamHorrorExpansionsIndex = httpClient.get("/assets/expansions/index.json").body()
        return expansions.expansions
            .map { expansionId ->
                httpClient.get("/assets/expansions/$expansionId/data.json").body<ArkhamHorrorExpansion>()
            }
    }
}
