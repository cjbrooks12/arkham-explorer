package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.config.ArkhamConfig
import com.caseyjbrooks.arkham.config.ArkhamConfigImpl
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerEventHandler
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerInputHandler
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerViewModel
import com.caseyjbrooks.arkham.ui.main.RouterViewModel
import com.caseyjbrooks.arkham.utils.resourceloader.KtorResourceLoader
import com.caseyjbrooks.arkham.utils.resourceloader.ResourceLoader
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.plusAssign
import kotlinx.coroutines.CoroutineScope

class ArkhamInjectorImpl(private val applicationCoroutineScope: CoroutineScope) : ArkhamInjector {
    private val config: ArkhamConfig = ArkhamConfigImpl()
    private val resourceLoader: ResourceLoader = KtorResourceLoader()

    private val router: RouterViewModel = RouterViewModel(defaultConfigBuilder().apply {
        this += LoggingInterceptor()
    }, applicationCoroutineScope)

    private fun defaultConfigBuilder(): BallastViewModelConfiguration.Builder {
        return BallastViewModelConfiguration.Builder()
            .apply {
                logger = ::PrintlnLogger
            }
    }

    override fun arkhamExplorerViewModel(coroutineScope: CoroutineScope): ArkhamExplorerViewModel {
        return ArkhamExplorerViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder(),
            inputHandler = ArkhamExplorerInputHandler(
                config = config,
                resourceLoader = resourceLoader,
            ),
            eventHandler = ArkhamExplorerEventHandler()
        )
    }

    override fun routerViewModel(): RouterViewModel {
        return router
    }
}
