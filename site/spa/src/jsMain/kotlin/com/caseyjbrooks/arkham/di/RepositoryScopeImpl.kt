package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.api.ArkhamExplorerApiImpl
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerContract
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerInputHandler
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepositoryImpl
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.navigation.BasicRouter
import com.copperleaf.ballast.navigation.Router
import com.copperleaf.ballast.navigation.browser.BrowserHashNavigationInterceptor
import com.copperleaf.ballast.navigation.browser.BrowserHistoryNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.NavGraph
import com.copperleaf.ballast.navigation.routing.fromEnum
import com.copperleaf.ballast.navigation.vm.RouterContract
import com.copperleaf.ballast.navigation.vm.withRouter
import com.copperleaf.ballast.plusAssign

class RepositoryScopeImpl(
    override val singletonScope: SingletonScope,
) : RepositoryScope {

    private val router: Router<ArkhamApp> = BasicRouter(
        coroutineScope = singletonScope.applicationCoroutineScope,
        config = singletonScope
            .defaultConfigBuilder<RouterContract.Inputs<ArkhamApp>, RouterContract.Events<ArkhamApp>, RouterContract.State<ArkhamApp>>(
                additionalConfig = {
                    if (singletonScope.config.useHistoryApi) {
                        this += BrowserHistoryNavigationInterceptor(singletonScope.config.basePath, ArkhamApp.Home)
                    } else {
                        this += BrowserHashNavigationInterceptor(ArkhamApp.Home)
                    }
                },
            )
            .withRouter(NavGraph.fromEnum(ArkhamApp.values()), null)
            .build(),
        eventHandler = eventHandler { },
    )

    private val arkhamExplorerRepository: ArkhamExplorerRepository = ArkhamExplorerRepositoryImpl(
        coroutineScope = singletonScope.applicationCoroutineScope,
        config = singletonScope.defaultConfig(
            initialState = ArkhamExplorerContract.State(),
            inputHandler = ArkhamExplorerInputHandler(),
            name = "Arkham Explorer Repository",
            additionalConfig = { inputStrategy = FifoInputStrategy() }
        ),
        eventBus = singletonScope.eventBus,
        api = ArkhamExplorerApiImpl(
            config = singletonScope.config,
            httpClient = singletonScope.httpClient,
        ),
    )

    override fun routerViewModel(): Router<ArkhamApp> {
        return router
    }

    override fun arkhamExplorerRepository(): ArkhamExplorerRepository {
        return arkhamExplorerRepository
    }
}
