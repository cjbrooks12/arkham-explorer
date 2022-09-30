package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.ui.encountersets.detail.EncounterSetDetailsContract
import com.caseyjbrooks.arkham.ui.encountersets.detail.EncounterSetDetailsInputHandler
import com.caseyjbrooks.arkham.ui.encountersets.detail.EncounterSetDetailsViewModel
import com.caseyjbrooks.arkham.ui.encountersets.list.EncounterSetsContract
import com.caseyjbrooks.arkham.ui.encountersets.list.EncounterSetsInputHandler
import com.caseyjbrooks.arkham.ui.encountersets.list.EncounterSetsViewModel
import com.caseyjbrooks.arkham.ui.error.NavigationErrorContract
import com.caseyjbrooks.arkham.ui.error.NavigationErrorInputHandler
import com.caseyjbrooks.arkham.ui.error.NavigationErrorViewModel
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
import com.caseyjbrooks.arkham.ui.products.detail.ProductDetailsContract
import com.caseyjbrooks.arkham.ui.products.detail.ProductDetailsInputHandler
import com.caseyjbrooks.arkham.ui.products.detail.ProductDetailsViewModel
import com.caseyjbrooks.arkham.ui.products.list.ProductsContract
import com.caseyjbrooks.arkham.ui.products.list.ProductsInputHandler
import com.caseyjbrooks.arkham.ui.products.list.ProductsViewModel
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsContract
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsInputHandler
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsViewModel
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosContract
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosInputHandler
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosViewModel
import com.caseyjbrooks.arkham.ui.tools.campaignlog.CampaignLogContract
import com.caseyjbrooks.arkham.ui.tools.campaignlog.CampaignLogInputHandler
import com.caseyjbrooks.arkham.ui.tools.campaignlog.CampaignLogSavedStateAdapter
import com.caseyjbrooks.arkham.ui.tools.campaignlog.CampaignLogViewModel
import com.caseyjbrooks.arkham.ui.tools.cards.CustomCardsContract
import com.caseyjbrooks.arkham.ui.tools.cards.CustomCardsInputHandler
import com.caseyjbrooks.arkham.ui.tools.cards.CustomCardsViewModel
import com.caseyjbrooks.arkham.ui.tools.chaosbag.ChaosBagSimulatorContract
import com.caseyjbrooks.arkham.ui.tools.chaosbag.ChaosBagSimulatorInputHandler
import com.caseyjbrooks.arkham.ui.tools.chaosbag.ChaosBagSimulatorViewModel
import com.caseyjbrooks.arkham.ui.tools.investigatortracker.InvestigatorTrackerContract
import com.caseyjbrooks.arkham.ui.tools.investigatortracker.InvestigatorTrackerInputHandler
import com.caseyjbrooks.arkham.ui.tools.investigatortracker.InvestigatorTrackerViewModel
import com.caseyjbrooks.arkham.utils.canvas.CanvasContract
import com.caseyjbrooks.arkham.utils.canvas.CanvasInputHandler
import com.caseyjbrooks.arkham.utils.canvas.CanvasSavedStateAdapter
import com.caseyjbrooks.arkham.utils.canvas.CanvasViewModel
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.InvestigatorId
import com.copperleaf.arkham.models.api.ProductId
import com.copperleaf.arkham.models.api.ScenarioId
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.savedstate.BallastSavedStateInterceptor
import kotlinx.coroutines.CoroutineScope

class ViewModelScopeImpl(
    override val repositoryScope: RepositoryScope,
) : ViewModelScope {

    override fun navigationErrorViewModel(
        coroutineScope: CoroutineScope
    ): NavigationErrorViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                initialState = NavigationErrorContract.State(),
                inputHandler = NavigationErrorInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                name = "Arkham Explorer",
            ) { NavigationErrorContract.Inputs.Initialize },
            eventHandler = eventHandler { },
        )
    }

    override fun homeViewModel(
        coroutineScope: CoroutineScope
    ): HomeViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = HomeInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = HomeContract.State(),
                name = "Arkham Explorer",
            ) { HomeContract.Inputs.Initialize },
            eventHandler = eventHandler { },
        )
    }

    override fun expansionsViewModel(
        coroutineScope: CoroutineScope
    ): ExpansionsViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = ExpansionsInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = ExpansionsContract.State(),
                name = "Arkham Explorer",
            ) { ExpansionsContract.Inputs.Initialize },
            eventHandler = eventHandler { },
        )
    }

    override fun expansionDetailsViewModel(
        coroutineScope: CoroutineScope,
        expansionCode: String
    ): ExpansionDetailsViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = ExpansionDetailsInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = ExpansionDetailsContract.State(),
                name = "Arkham Explorer",
            ) { ExpansionDetailsContract.Inputs.Initialize(expansionCode) },
            eventHandler = eventHandler { },
        )
    }

    override fun scenariosViewModel(
        coroutineScope: CoroutineScope
    ): ScenariosViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = ScenariosInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = ScenariosContract.State(),
                name = "Arkham Explorer",
            ) { ScenariosContract.Inputs.Initialize },
            eventHandler = eventHandler { },
        )
    }

    override fun scenarioDetailsViewModel(
        coroutineScope: CoroutineScope,
        scenarioId: ScenarioId,
    ): ScenarioDetailsViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = ScenarioDetailsInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = ScenarioDetailsContract.State(),
                name = "Arkham Explorer",
            ) { ScenarioDetailsContract.Inputs.Initialize(scenarioId) },
            eventHandler = eventHandler { },
        )
    }

    override fun encounterSetsViewModel(
        coroutineScope: CoroutineScope
    ): EncounterSetsViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = EncounterSetsInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository(),
                ),
                initialState = EncounterSetsContract.State(),
                name = "Arkham Explorer",
            ) { EncounterSetsContract.Inputs.Initialize },
            eventHandler = eventHandler { },
        )
    }

    override fun encounterSetDetailsViewModel(
        coroutineScope: CoroutineScope,
        encounterSetId: EncounterSetId
    ): EncounterSetDetailsViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = EncounterSetDetailsInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = EncounterSetDetailsContract.State(),
                name = "Arkham Explorer",
            ) { EncounterSetDetailsContract.Inputs.Initialize(encounterSetId) },
            eventHandler = eventHandler { },
        )
    }

    override fun investigatorsViewModel(
        coroutineScope: CoroutineScope
    ): InvestigatorsViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = InvestigatorsInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = InvestigatorsContract.State(),
                name = "Arkham Explorer",
            ) { InvestigatorsContract.Inputs.Initialize },
            eventHandler = eventHandler { },
        )
    }

    override fun investigatorDetailsViewModel(
        coroutineScope: CoroutineScope,
        investigatorId: InvestigatorId
    ): InvestigatorDetailsViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = InvestigatorDetailsInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = InvestigatorDetailsContract.State(),
                name = "Arkham Explorer",
            ) { InvestigatorDetailsContract.Inputs.Initialize(investigatorId) },
            eventHandler = eventHandler { },
        )
    }

    override fun productsViewModel(
        coroutineScope: CoroutineScope
    ): ProductsViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = ProductsInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = ProductsContract.State(),
                name = "Products",
            ) { ProductsContract.Inputs.Initialize },
            eventHandler = eventHandler { }
        )
    }

    override fun productDetailsViewModel(
        coroutineScope: CoroutineScope,
        productId: ProductId,
    ): ProductDetailsViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = ProductDetailsInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = ProductDetailsContract.State(),
                name = "Arkham Explorer",
            ) { ProductDetailsContract.Inputs.Initialize(productId) },
            eventHandler = eventHandler { },
        )
    }

    override fun staticPageViewModel(
        coroutineScope: CoroutineScope,
        slug: String,
    ): StaticPageViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = StaticPageInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = StaticPageContract.State(),
                name = "Static Page",
            ) { StaticPageContract.Inputs.Initialize(slug) },
            eventHandler = eventHandler { },
        )
    }

    override fun chaosBagSimulatorViewModel(
        coroutineScope: CoroutineScope,
        scenarioId: ScenarioId?
    ): ChaosBagSimulatorViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = ChaosBagSimulatorInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = ChaosBagSimulatorContract.State(),
                name = "Chaos Bag Simulator",
            ) {
                if (scenarioId != null) {
                    ChaosBagSimulatorContract.Inputs.InitializeForScenario(scenarioId)
                } else {
                    ChaosBagSimulatorContract.Inputs.InitializeDefault
                }
            },
            eventHandler = eventHandler { },
        )
    }

    override fun campaignLogViewModel(
        coroutineScope: CoroutineScope,
        expansionCode: String?,
        campaignLogId: String?,
    ): CampaignLogViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = CampaignLogInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = CampaignLogContract.State(),
                name = "Campaign Log",
                additionalConfig = {
                    this.inputStrategy = FifoInputStrategy()
                    this += BallastSavedStateInterceptor(CampaignLogSavedStateAdapter(expansionCode, campaignLogId))
                }
            ),
            eventHandler = eventHandler { },
        )
    }

    override fun investigatorTrackerViewModel(
        coroutineScope: CoroutineScope,
        investigatorId: InvestigatorId?
    ): InvestigatorTrackerViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = InvestigatorTrackerInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = InvestigatorTrackerContract.State(),
                name = "Investigator Tracker",
            ) {
                if (investigatorId != null) {
                    InvestigatorTrackerContract.Inputs.InitializeForInvestigator(investigatorId)
                } else {
                    InvestigatorTrackerContract.Inputs.InitializeDefault
                }
            },
            eventHandler = eventHandler { },
        )
    }

    override fun customCardsViewModel(coroutineScope: CoroutineScope): CustomCardsViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = CustomCardsInputHandler(
                    repository = repositoryScope.arkhamExplorerRepository()
                ),
                initialState = CustomCardsContract.State(),
                name = "Custom Cards",
            ) { CustomCardsContract.Inputs.Initialize },
            eventHandler = eventHandler { },
        )
    }

    override fun canvasViewModel(coroutineScope: CoroutineScope): CanvasViewModel {
        return BasicViewModel(
            coroutineScope = coroutineScope,
            config = repositoryScope.singletonScope.defaultConfigBuilder(
                inputHandler = CanvasInputHandler(
                    loadFormDefinition = {
                        repositoryScope.arkhamExplorerRepository().getFormDefinition(false, "assets")
                    },
                    loadCanvasDefinition = {
                        repositoryScope.arkhamExplorerRepository().getCanvasDefinition(false, "assets")
                    },
                ),
                initialState = CanvasContract.State(),
                name = "Custom Cards",
                additionalConfig = {
                    this += BallastSavedStateInterceptor(
                        CanvasSavedStateAdapter(name)
                    )
                }
            ),
            eventHandler = eventHandler { },
        )
    }
}

