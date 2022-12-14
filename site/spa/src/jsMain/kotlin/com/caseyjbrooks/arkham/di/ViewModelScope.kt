package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.ui.encountersets.detail.EncounterSetDetailsViewModel
import com.caseyjbrooks.arkham.ui.encountersets.list.EncounterSetsViewModel
import com.caseyjbrooks.arkham.ui.error.NavigationErrorViewModel
import com.caseyjbrooks.arkham.ui.expansions.detail.ExpansionDetailsViewModel
import com.caseyjbrooks.arkham.ui.expansions.list.ExpansionsViewModel
import com.caseyjbrooks.arkham.ui.home.HomeViewModel
import com.caseyjbrooks.arkham.ui.investigators.detail.InvestigatorDetailsViewModel
import com.caseyjbrooks.arkham.ui.investigators.list.InvestigatorsViewModel
import com.caseyjbrooks.arkham.ui.pages.StaticPageViewModel
import com.caseyjbrooks.arkham.ui.products.detail.ProductDetailsViewModel
import com.caseyjbrooks.arkham.ui.products.list.ProductsViewModel
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsViewModel
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosViewModel
import com.caseyjbrooks.arkham.ui.tools.campaignlog.CampaignLogViewModel
import com.caseyjbrooks.arkham.ui.tools.cards.CustomCardsViewModel
import com.caseyjbrooks.arkham.ui.tools.chaosbag.ChaosBagSimulatorViewModel
import com.caseyjbrooks.arkham.ui.tools.investigatortracker.InvestigatorTrackerViewModel
import com.caseyjbrooks.arkham.utils.canvas.CanvasViewModel
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.InvestigatorId
import com.copperleaf.arkham.models.api.ProductId
import com.copperleaf.arkham.models.api.ScenarioId
import kotlinx.coroutines.CoroutineScope

interface ViewModelScope {
    val repositoryScope: RepositoryScope

    fun navigationErrorViewModel(coroutineScope: CoroutineScope): NavigationErrorViewModel
    fun homeViewModel(coroutineScope: CoroutineScope): HomeViewModel

    fun expansionsViewModel(coroutineScope: CoroutineScope): ExpansionsViewModel
    fun expansionDetailsViewModel(coroutineScope: CoroutineScope, expansionCode: String): ExpansionDetailsViewModel

    fun scenariosViewModel(coroutineScope: CoroutineScope): ScenariosViewModel
    fun scenarioDetailsViewModel(coroutineScope: CoroutineScope, scenarioId: ScenarioId): ScenarioDetailsViewModel

    fun encounterSetsViewModel(coroutineScope: CoroutineScope): EncounterSetsViewModel
    fun encounterSetDetailsViewModel(
        coroutineScope: CoroutineScope,
        encounterSetId: EncounterSetId
    ): EncounterSetDetailsViewModel

    fun investigatorsViewModel(coroutineScope: CoroutineScope): InvestigatorsViewModel
    fun investigatorDetailsViewModel(
        coroutineScope: CoroutineScope,
        investigatorId: InvestigatorId
    ): InvestigatorDetailsViewModel

    fun productsViewModel(coroutineScope: CoroutineScope): ProductsViewModel
    fun productDetailsViewModel(coroutineScope: CoroutineScope, productId: ProductId): ProductDetailsViewModel

    fun staticPageViewModel(coroutineScope: CoroutineScope, slug: String): StaticPageViewModel

    fun chaosBagSimulatorViewModel(coroutineScope: CoroutineScope, scenarioId: ScenarioId?): ChaosBagSimulatorViewModel
    fun campaignLogViewModel(
        coroutineScope: CoroutineScope,
        expansionCode: String?,
        campaignLogId: String?
    ): CampaignLogViewModel

    fun investigatorTrackerViewModel(
        coroutineScope: CoroutineScope,
        investigatorId: InvestigatorId?
    ): InvestigatorTrackerViewModel

    fun customCardsViewModel(coroutineScope: CoroutineScope): CustomCardsViewModel
    fun canvasViewModel(coroutineScope: CoroutineScope): CanvasViewModel
}
