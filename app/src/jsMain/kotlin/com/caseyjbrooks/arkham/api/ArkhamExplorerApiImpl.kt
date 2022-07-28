package com.caseyjbrooks.arkham.api

import com.caseyjbrooks.arkham.config.ArkhamConfig
import com.caseyjbrooks.arkham.models.ArkhamHorrorExpansion
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.delay

class ArkhamExplorerApiImpl(
    private val config: ArkhamConfig,
    private val httpClient: HttpClient,
) : ArkhamExplorerApi {
    override suspend fun getExpansions(): List<ArkhamHorrorExpansion> {
        delay(2000)
        return config
            .getExpansions()
            .map { expansionUrl ->
                httpClient.get(expansionUrl).body<ArkhamHorrorExpansion>()
            }
    }
}
