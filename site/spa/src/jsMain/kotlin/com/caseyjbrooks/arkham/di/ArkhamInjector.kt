package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.config.ArkhamConfig
import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.ui.RouterViewModel
import com.caseyjbrooks.arkham.ui.encountersets.detail.EncounterSetDetailsViewModel
import com.caseyjbrooks.arkham.ui.encountersets.list.EncounterSetsViewModel
import com.caseyjbrooks.arkham.ui.expansions.detail.ExpansionDetailsViewModel
import com.caseyjbrooks.arkham.ui.expansions.list.ExpansionsViewModel
import com.caseyjbrooks.arkham.ui.home.HomeViewModel
import com.caseyjbrooks.arkham.ui.investigators.detail.InvestigatorDetailsViewModel
import com.caseyjbrooks.arkham.ui.investigators.list.InvestigatorsViewModel
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsViewModel
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosViewModel
import com.caseyjbrooks.arkham.utils.navigation.NavigationLinkStrategy
import kotlinx.coroutines.CoroutineScope

interface ArkhamInjector {
    val config: ArkhamConfig
    val navigationLinkStrategy: NavigationLinkStrategy

    fun routerViewModel(): RouterViewModel
    fun arkhamExplorerRepository(): ArkhamExplorerRepository

    fun homeViewModel(coroutineScope: CoroutineScope): HomeViewModel

    fun expansionsViewModel(coroutineScope: CoroutineScope): ExpansionsViewModel
    fun expansionDetailsViewModel(coroutineScope: CoroutineScope, expansionId: String): ExpansionDetailsViewModel

    fun investigatorsViewModel(coroutineScope: CoroutineScope): InvestigatorsViewModel
    fun investigatorDetailsViewModel(coroutineScope: CoroutineScope, investigatorId: String): InvestigatorDetailsViewModel

    fun scenariosViewModel(coroutineScope: CoroutineScope): ScenariosViewModel
    fun scenarioDetailsViewModel(coroutineScope: CoroutineScope, scenarioId: String): ScenarioDetailsViewModel

    fun encounterSetsViewModel(coroutineScope: CoroutineScope): EncounterSetsViewModel
    fun encounterSetDetailsViewModel(coroutineScope: CoroutineScope, encounterSetId: String): EncounterSetDetailsViewModel
}