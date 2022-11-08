package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.config.ArkhamConfig
import com.caseyjbrooks.arkham.utils.navigation.HashNavigationLinkStrategy
import com.caseyjbrooks.arkham.utils.navigation.HistoryNavigationLinkStrategy
import com.caseyjbrooks.arkham.utils.navigation.NavigationLinkStrategy
import com.copperleaf.ballast.core.JsConsoleBallastLogger
import com.copperleaf.ballast.debugger.BallastDebuggerClientConnection
import com.copperleaf.ballast.repository.bus.EventBusImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

class SingletonScopeImpl(
    override val applicationCoroutineScope: CoroutineScope,
    override val config: ArkhamConfig,
) : SingletonScope {
    override val eventBus = EventBusImpl()
    override val httpClient = HttpClient(Js) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            url("${config.baseUrl}/")
        }
    }

    override val ballastLogger = JsConsoleBallastLogger()

    override val navigationLinkStrategy: NavigationLinkStrategy = if (config.useHistoryApi) {
        HistoryNavigationLinkStrategy
    } else {
        HashNavigationLinkStrategy
    }

    override val debuggerConnection = BallastDebuggerClientConnection(
        Js,
        applicationCoroutineScope,
        host = "127.0.0.1",
    ).also { it.connect() }
}

