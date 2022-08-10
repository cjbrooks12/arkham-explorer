package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.api.ArkhamExplorerApiImpl
import com.caseyjbrooks.arkham.config.ArkhamConfig
import com.caseyjbrooks.arkham.config.ArkhamConfigImpl
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerInputHandler
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepositoryImpl
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.ui.RouterViewModel
import com.caseyjbrooks.arkham.ui.encountersets.detail.EncounterSetDetailsContract
import com.caseyjbrooks.arkham.ui.encountersets.detail.EncounterSetDetailsInputHandler
import com.caseyjbrooks.arkham.ui.encountersets.detail.EncounterSetDetailsViewModel
import com.caseyjbrooks.arkham.ui.encountersets.list.EncounterSetsContract
import com.caseyjbrooks.arkham.ui.encountersets.list.EncounterSetsInputHandler
import com.caseyjbrooks.arkham.ui.encountersets.list.EncounterSetsViewModel
import com.caseyjbrooks.arkham.ui.expansions.detail.ExpansionDetailsContract
import com.caseyjbrooks.arkham.ui.expansions.detail.ExpansionDetailsInputHandler
import com.caseyjbrooks.arkham.ui.expansions.detail.ExpansionDetailsViewModel
import com.caseyjbrooks.arkham.ui.expansions.list.ExpansionsContract
import com.caseyjbrooks.arkham.ui.expansions.list.ExpansionsInputHandler
import com.caseyjbrooks.arkham.ui.expansions.list.ExpansionsViewModel
import com.caseyjbrooks.arkham.ui.home.HomeContract
import com.caseyjbrooks.arkham.ui.home.HomeInputHandler
import com.caseyjbrooks.arkham.ui.home.HomeViewModel
import com.caseyjbrooks.arkham.ui.investigators.detail.InvestigatorDetailsContract
import com.caseyjbrooks.arkham.ui.investigators.detail.InvestigatorDetailsInputHandler
import com.caseyjbrooks.arkham.ui.investigators.detail.InvestigatorDetailsViewModel
import com.caseyjbrooks.arkham.ui.investigators.list.InvestigatorsContract
import com.caseyjbrooks.arkham.ui.investigators.list.InvestigatorsInputHandler
import com.caseyjbrooks.arkham.ui.investigators.list.InvestigatorsViewModel
import com.caseyjbrooks.arkham.ui.pages.StaticPageContract
import com.caseyjbrooks.arkham.ui.pages.StaticPageInputHandler
import com.caseyjbrooks.arkham.ui.pages.StaticPageViewModel
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsContract
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsInputHandler
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsViewModel
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosContract
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosInputHandler
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosViewModel
import com.caseyjbrooks.arkham.utils.navigation.HashNavigationLinkStrategy
import com.caseyjbrooks.arkham.utils.navigation.HistoryNavigationLinkStrategy
import com.caseyjbrooks.arkham.utils.navigation.NavigationLinkStrategy
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.InvestigatorId
import com.copperleaf.arkham.models.api.ScenarioId
import com.copperleaf.ballast.BallastLogger
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.navigation.routing.BrowserHashNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.BrowserHistoryNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.withRouter
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.repository.bus.EventBusImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

class ArkhamInjectorImpl(
    private val applicationCoroutineScope: CoroutineScope,
    private val isPwa: Boolean,
) : ArkhamInjector {
    override val config: ArkhamConfig = ArkhamConfigImpl()
    private val eventBus = EventBusImpl()
    private val httpClient = HttpClient(Js) {
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

    override val navigationLinkStrategy: NavigationLinkStrategy = if (config.useHistoryApi) {
        HistoryNavigationLinkStrategy
    } else {
        HashNavigationLinkStrategy
    }
    private val router: RouterViewModel = RouterViewModel(
        config = defaultConfigBuilder()
            .withRouter(ArkhamApp)
            .apply {
                if (config.useHistoryApi) {
                    this += BrowserHistoryNavigationInterceptor(config.basePath)
                } else {
                    this += BrowserHashNavigationInterceptor()
                }
            },
        coroutineScope = applicationCoroutineScope,
    )

    private val arkhamExplorerRepository: ArkhamExplorerRepository = ArkhamExplorerRepositoryImpl(
        coroutineScope = applicationCoroutineScope,
        configBuilder = defaultConfigBuilder(),
        inputHandler = ArkhamExplorerInputHandler(),
        eventBus = eventBus,
        api = ArkhamExplorerApiImpl(
            config = config,
            httpClient = httpClient,
        ),
    )

    override fun routerViewModel(): RouterViewModel {
        return router
    }

    override fun arkhamExplorerRepository(): ArkhamExplorerRepository {
        return arkhamExplorerRepository
    }

    override fun homeViewModel(
        coroutineScope: CoroutineScope
    ): HomeViewModel {
        return HomeViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { HomeContract.Inputs.Initialize }
                },
            inputHandler = HomeInputHandler(
                repository = arkhamExplorerRepository
            ),
        )
    }

    override fun expansionsViewModel(
        coroutineScope: CoroutineScope
    ): ExpansionsViewModel {
        return ExpansionsViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { ExpansionsContract.Inputs.Initialize }
                },
            inputHandler = ExpansionsInputHandler(
                repository = arkhamExplorerRepository
            ),
        )
    }

    override fun expansionDetailsViewModel(
        coroutineScope: CoroutineScope,
        expansionCode: String
    ): ExpansionDetailsViewModel {
        return ExpansionDetailsViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { ExpansionDetailsContract.Inputs.Initialize(expansionCode) }
                },
            inputHandler = ExpansionDetailsInputHandler(
                repository = arkhamExplorerRepository
            ),
        )
    }

    override fun investigatorsViewModel(
        coroutineScope: CoroutineScope
    ): InvestigatorsViewModel {
        return InvestigatorsViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { InvestigatorsContract.Inputs.Initialize }
                },
            inputHandler = InvestigatorsInputHandler(
                repository = arkhamExplorerRepository
            ),
        )
    }

    override fun investigatorDetailsViewModel(
        coroutineScope: CoroutineScope,
        investigatorId: InvestigatorId
    ): InvestigatorDetailsViewModel {
        return InvestigatorDetailsViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { InvestigatorDetailsContract.Inputs.Initialize(investigatorId) }
                },
            inputHandler = InvestigatorDetailsInputHandler(
                repository = arkhamExplorerRepository
            ),
        )
    }

    override fun scenariosViewModel(
        coroutineScope: CoroutineScope
    ): ScenariosViewModel {
        return ScenariosViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { ScenariosContract.Inputs.Initialize }
                },
            inputHandler = ScenariosInputHandler(
                repository = arkhamExplorerRepository
            ),
        )
    }

    override fun scenarioDetailsViewModel(
        coroutineScope: CoroutineScope,
        scenarioId: ScenarioId,
    ): ScenarioDetailsViewModel {
        return ScenarioDetailsViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { ScenarioDetailsContract.Inputs.Initialize(scenarioId) }
                },
            inputHandler = ScenarioDetailsInputHandler(
                repository = arkhamExplorerRepository
            ),
        )
    }

    override fun encounterSetsViewModel(
        coroutineScope: CoroutineScope
    ): EncounterSetsViewModel {
        return EncounterSetsViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { EncounterSetsContract.Inputs.Initialize }
                },
            inputHandler = EncounterSetsInputHandler(
                repository = arkhamExplorerRepository
            ),
        )
    }

    override fun encounterSetDetailsViewModel(
        coroutineScope: CoroutineScope,
        encounterSetId: EncounterSetId
    ): EncounterSetDetailsViewModel {
        return EncounterSetDetailsViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { EncounterSetDetailsContract.Inputs.Initialize(encounterSetId) }
                },
            inputHandler = EncounterSetDetailsInputHandler(
                repository = arkhamExplorerRepository
            ),
        )
    }

    override fun staticPageViewModel(
        coroutineScope: CoroutineScope,
        slug: String,
    ): StaticPageViewModel {
        return StaticPageViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { StaticPageContract.Inputs.Initialize(slug) }
                },
            inputHandler = StaticPageInputHandler(
                repository = arkhamExplorerRepository
            ),
        )
    }

// Helpers
// ---------------------------------------------------------------------------------------------------------------------

    private fun defaultConfigBuilder(): BallastViewModelConfiguration.Builder {
        return BallastViewModelConfiguration.Builder()
            .apply {
                logger = {
                    object : BallastLogger {
                        override fun debug(message: String) {}
                        override fun info(message: String) {}
                        override fun error(throwable: Throwable) {
                            console.error(throwable)
                        }
                    }
                }
                this += LoggingInterceptor()
            }
    }
}

