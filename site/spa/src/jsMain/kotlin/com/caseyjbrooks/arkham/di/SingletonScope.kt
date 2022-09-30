package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.config.ArkhamConfig
import com.caseyjbrooks.arkham.utils.navigation.NavigationLinkStrategy
import com.copperleaf.ballast.BallastLogger
import com.copperleaf.ballast.debugger.BallastDebuggerClientConnection
import com.copperleaf.ballast.repository.bus.EventBus
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineConfig
import kotlinx.coroutines.CoroutineScope

interface SingletonScope {
    val applicationCoroutineScope: CoroutineScope
    val config: ArkhamConfig
    val ballastLogger: BallastLogger
    val navigationLinkStrategy: NavigationLinkStrategy
    val eventBus: EventBus
    val httpClient: HttpClient
    val debuggerConnection: BallastDebuggerClientConnection<HttpClientEngineConfig>
}

