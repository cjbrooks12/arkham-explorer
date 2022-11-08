package com.caseyjbrooks.arkham.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.encountersets.detail.EncounterSetDetailsUi
import com.caseyjbrooks.arkham.ui.encountersets.list.EncounterSetsUi
import com.caseyjbrooks.arkham.ui.error.NavigationErrorUi
import com.caseyjbrooks.arkham.ui.expansions.detail.ExpansionDetailsUi
import com.caseyjbrooks.arkham.ui.expansions.list.ExpansionsUi
import com.caseyjbrooks.arkham.ui.home.HomeUi
import com.caseyjbrooks.arkham.ui.investigators.detail.InvestigatorDetailsUi
import com.caseyjbrooks.arkham.ui.investigators.list.InvestigatorsUi
import com.caseyjbrooks.arkham.ui.pages.StaticPageUi
import com.caseyjbrooks.arkham.ui.products.detail.ProductDetailsUi
import com.caseyjbrooks.arkham.ui.products.list.ProductsUi
import com.caseyjbrooks.arkham.ui.scenarios.detail.ScenarioDetailsUi
import com.caseyjbrooks.arkham.ui.scenarios.list.ScenariosUi
import com.caseyjbrooks.arkham.ui.tools.campaignlog.CampaignLogUi
import com.caseyjbrooks.arkham.ui.tools.cards.CustomCardsUi
import com.caseyjbrooks.arkham.ui.tools.chaosbag.ChaosBagSimulatorUi
import com.caseyjbrooks.arkham.ui.tools.dividers.DividersGeneratorUi
import com.caseyjbrooks.arkham.ui.tools.investigatortracker.InvestigatorTrackerUi
import com.caseyjbrooks.arkham.ui.tools.list.ToolsListUi
import com.caseyjbrooks.arkham.ui.tools.tuckbox.TuckboxGeneratorUi
import com.caseyjbrooks.arkham.utils.theme.ArkhamTheme
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.InvestigatorId
import com.copperleaf.arkham.models.api.ProductId
import com.copperleaf.arkham.models.api.ScenarioId
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.vm.optionalStringQuery
import com.copperleaf.ballast.navigation.vm.renderCurrentDestination
import com.copperleaf.ballast.navigation.vm.stringPath
import org.jetbrains.compose.web.dom.Text

val LocalInjector = staticCompositionLocalOf<ArkhamInjector> { error("LocalInjector not provided") }

@Composable
fun MainApplication(injector: ArkhamInjector) {
    ArkhamTheme(injector) {
        val routerVm = remember(injector) { injector.routerViewModel() }
        val routerVmState by routerVm.observeStates().collectAsState()

        routerVmState.renderCurrentDestination(
            displayRoute = {
                MainApplicationRouteMatch(injector)
            },
            displayRouteNotFound = {
                NavigationErrorUi.Page(injector) {
                    Text("$it not found")
                }
            },
        )
    }
}

@Composable
fun Destination.Match<ArkhamApp>.MainApplicationRouteMatch(injector: ArkhamInjector) {
    when (originalRoute) {
        ArkhamApp.Home -> {
            HomeUi.Page(injector)
        }

        ArkhamApp.Expansions -> {
            ExpansionsUi.Page(injector)
        }

        ArkhamApp.ExpansionDetails -> {
            val expansionCode by stringPath()
            ExpansionDetailsUi.Page(
                injector,
                expansionCode,
            )
        }

        ArkhamApp.Scenarios -> {
            ScenariosUi.Page(injector)
        }

        ArkhamApp.ScenarioDetails -> {
            val scenarioId by stringPath()
            ScenarioDetailsUi.Page(
                injector,
                ScenarioId(scenarioId),
            )
        }

        ArkhamApp.EncounterSets -> {
            EncounterSetsUi.Page(injector)
        }

        ArkhamApp.EncounterSetDetails -> {
            val encounterSetId by stringPath()
            EncounterSetDetailsUi.Page(
                injector,
                EncounterSetId(encounterSetId),
            )
        }

        ArkhamApp.Investigators -> {
            InvestigatorsUi.Page(injector)
        }

        ArkhamApp.InvestigatorDetails -> {
            val investigatorId by stringPath()
            InvestigatorDetailsUi.Page(
                injector,
                InvestigatorId(investigatorId),
            )
        }

        ArkhamApp.Products -> {
            ProductsUi.Page(injector)
        }

        ArkhamApp.ProductDetails -> {
            val productId by stringPath()
            ProductDetailsUi.Page(
                injector,
                ProductId(productId),
            )
        }

        ArkhamApp.StaticPage -> {
            val slug by stringPath()
            StaticPageUi.Page(
                injector,
                slug,
            )
        }

        ArkhamApp.Tools -> {
            ToolsListUi.Page(injector)
        }

        ArkhamApp.ChaosBagSimulator -> {
            val scenarioId by optionalStringQuery()
            ChaosBagSimulatorUi.Page(injector, scenarioId?.let { ScenarioId(it) })
        }

        ArkhamApp.InvestigatorTracker -> {
            val investigatorId by optionalStringQuery()
            InvestigatorTrackerUi.Page(injector, investigatorId?.let { InvestigatorId(it) })
        }

        ArkhamApp.AboutCampaignLog -> {
            CampaignLogUi.Page(injector)
        }

        ArkhamApp.CreateCampaignLog -> {
            val expansionCode by stringPath()
            CampaignLogUi.Page(
                injector,
                expansionCode
            )
        }

        ArkhamApp.ViewCampaignLog -> {
            val expansionCode by stringPath()
            val campaignLogId by stringPath()
            CampaignLogUi.Page(
                injector,
                expansionCode,
                campaignLogId,
            )
        }

        ArkhamApp.DividersGenerator -> {
            DividersGeneratorUi.Page(injector)
        }

        ArkhamApp.TuckboxGenerator -> {
            TuckboxGeneratorUi.Page(injector)
        }

        ArkhamApp.CustomCards -> {
            CustomCardsUi.Page(injector)
        }
    }
}
