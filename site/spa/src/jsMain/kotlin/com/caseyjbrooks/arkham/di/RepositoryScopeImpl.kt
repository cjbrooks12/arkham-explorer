package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.api.ArkhamExplorerApiImpl
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerContract
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerInputHandler
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepositoryImpl
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.ui.RouterViewModel
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.navigation.routing.BrowserHashNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.BrowserHistoryNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.NavGraph
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.RouterInputHandler
import com.copperleaf.ballast.navigation.routing.asStartDestinationString
import com.copperleaf.ballast.plusAssign

class RepositoryScopeImpl(
    override val singletonScope: SingletonScope,
) : RepositoryScope {

    private val router: RouterViewModel = BasicViewModel(
        coroutineScope = singletonScope.applicationCoroutineScope,
        config = singletonScope.defaultConfigBuilder(
            inputHandler = RouterInputHandler(),
            initialState = RouterContract.State(
                navGraph = NavGraph(ArkhamApp)
            ),
            name = "Router",
            useDebugger = true,
            additionalConfig = {
                inputStrategy = FifoInputStrategy()

                if (singletonScope.config.useHistoryApi) {
                    this += BrowserHistoryNavigationInterceptor(singletonScope.config.basePath)
                } else {
                    this += BrowserHashNavigationInterceptor()
                }
            }
        ) {
            RouterContract.Inputs.GoToDestination(
                ArkhamApp.initialRoute.asStartDestinationString()
            )
        },
        eventHandler = eventHandler { }
    )

    private val arkhamExplorerRepository: ArkhamExplorerRepository = ArkhamExplorerRepositoryImpl(
        coroutineScope = singletonScope.applicationCoroutineScope,
        config = singletonScope.defaultConfigBuilder(
            initialState = ArkhamExplorerContract.State(),
            inputHandler = ArkhamExplorerInputHandler(),
            name = "Arkham Explorer",
        ),
        eventBus = singletonScope.eventBus,
        api = ArkhamExplorerApiImpl(
            config = singletonScope.config,
            httpClient = singletonScope.httpClient,
        ),
    )

    override fun routerViewModel(): RouterViewModel {
        return router
    }

    override fun arkhamExplorerRepository(): ArkhamExplorerRepository {
        return arkhamExplorerRepository
    }
}
