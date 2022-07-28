package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.api.ArkhamExplorerApiImpl
import com.caseyjbrooks.arkham.config.ArkhamConfig
import com.caseyjbrooks.arkham.config.ArkhamConfigImpl
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerInputHandler
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepositoryImpl
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
import com.caseyjbrooks.arkham.ui.investigators.detail.InvestigatorDetailsContract
import com.caseyjbrooks.arkham.ui.investigators.detail.InvestigatorDetailsInputHandler
import com.caseyjbrooks.arkham.ui.investigators.detail.InvestigatorDetailsViewModel
import com.caseyjbrooks.arkham.ui.investigators.list.InvestigatorsContract
import com.caseyjbrooks.arkham.ui.investigators.list.InvestigatorsInputHandler
import com.caseyjbrooks.arkham.ui.investigators.list.InvestigatorsViewModel
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsContract
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsInputHandler
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsViewModel
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosContract
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosInputHandler
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosViewModel
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.repository.bus.EventBusImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

class ArkhamInjectorImpl(private val applicationCoroutineScope: CoroutineScope) : ArkhamInjector {
    private val config: ArkhamConfig = ArkhamConfigImpl()
    private val eventBus = EventBusImpl()
    private val httpClient = HttpClient(Js) {
        install(ContentNegotiation) {
            json(Json)
        }
    }

    private val router: RouterViewModel = RouterViewModel(
        config = defaultConfigBuilder(),
        coroutineScope = applicationCoroutineScope,
    )

    private val arkhamExplorerRepository: ArkhamExplorerRepository = ArkhamExplorerRepositoryImpl(
        coroutineScope = applicationCoroutineScope,
        configBuilder = defaultConfigBuilder(),
        inputHandler = ArkhamExplorerInputHandler(
            api = ArkhamExplorerApiImpl(
                config = config,
                httpClient = httpClient,
            ),
            eventBus = eventBus,
        ),
        eventBus = eventBus,
    )

    override fun routerViewModel(): RouterViewModel {
        return router
    }

    override fun arkhamExplorerRepository(): ArkhamExplorerRepository {
        return arkhamExplorerRepository
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
        expansionId: String
    ): ExpansionDetailsViewModel {
        return ExpansionDetailsViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { ExpansionDetailsContract.Inputs.Initialize(expansionId) }
                },
            inputHandler = ExpansionDetailsInputHandler(),
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
        investigatorId: String
    ): InvestigatorDetailsViewModel {
        return InvestigatorDetailsViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { InvestigatorDetailsContract.Inputs.Initialize(investigatorId) }
                },
            inputHandler = InvestigatorDetailsInputHandler(),
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
        scenarioId: String
    ): ScenarioDetailsViewModel {
        return ScenarioDetailsViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { ScenarioDetailsContract.Inputs.Initialize(scenarioId) }
                },
            inputHandler = ScenarioDetailsInputHandler(),
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
        encounterSetId: String
    ): EncounterSetDetailsViewModel {
        return EncounterSetDetailsViewModel(
            coroutineScope = coroutineScope,
            configBuilder = defaultConfigBuilder()
                .apply {
                    this += BootstrapInterceptor { EncounterSetDetailsContract.Inputs.Initialize(encounterSetId) }
                },
            inputHandler = EncounterSetDetailsInputHandler(),
        )
    }

// Helpers
// ---------------------------------------------------------------------------------------------------------------------

    private fun defaultConfigBuilder(): BallastViewModelConfiguration.Builder {
        return BallastViewModelConfiguration.Builder()
            .apply {
                logger = ::PrintlnLogger
                this += LoggingInterceptor()
            }
    }
}
