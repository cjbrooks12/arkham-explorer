package com.caseyjbrooks.arkham.api

import com.caseyjbrooks.arkham.config.ArkhamConfig
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.arkham.models.ArkhamHorrorExpansionsIndex
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ArkhamExplorerApiImpl(
    private val config: ArkhamConfig,
    private val httpClient: HttpClient,
) : ArkhamExplorerApi {
    override suspend fun getExpansions(): List<ArkhamHorrorExpansion> {
        val expansions: ArkhamHorrorExpansionsIndex = httpClient.get("api/expansions.json").body()
        return expansions.expansions
            .map { expansion ->
                httpClient.get("api/expansions/${expansion.slug}.json").body<ArkhamHorrorExpansion>()
            }
    }
}
