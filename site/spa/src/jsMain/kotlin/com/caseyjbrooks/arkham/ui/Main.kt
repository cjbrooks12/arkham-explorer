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
import com.copperleaf.ballast.navigation.routing.MissingDestination
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNotFound
import org.jetbrains.compose.web.dom.Text

val LocalInjector = staticCompositionLocalOf<ArkhamInjector> { error("LocalInjector not provided") }

@Composable
fun MainApplication(injector: ArkhamInjector) {
    ArkhamTheme(injector) {
        val routerVm = remember(injector) { injector.routerViewModel() }
        val routerVmState by routerVm.observeStates().collectAsState()

        when (val destination = routerVmState.currentDestinationOrNotFound) {
            is Destination -> {
                when (destination.originalRoute) {
                    ArkhamApp.Home -> {
                        HomeUi.Page(injector)
                    }

                    ArkhamApp.Expansions -> {
                        ExpansionsUi.Page(injector)
                    }

                    ArkhamApp.ExpansionDetails -> {
                        ExpansionDetailsUi.Page(
                            injector,
                            destination.pathParameters["expansionCode"]!!.single(),
                        )
                    }

                    ArkhamApp.Scenarios -> {
                        ScenariosUi.Page(injector)
                    }

                    ArkhamApp.ScenarioDetails -> {
                        ScenarioDetailsUi.Page(
                            injector,
                            ScenarioId(
                                destination.pathParameters["scenarioId"]!!.single(),
                            )
                        )
                    }

                    ArkhamApp.EncounterSets -> {
                        EncounterSetsUi.Page(injector)
                    }

                    ArkhamApp.EncounterSetDetails -> {
                        EncounterSetDetailsUi.Page(
                            injector,
                            EncounterSetId(
                                destination.pathParameters["encounterSetId"]!!.single()
                            ),
                        )
                    }

                    ArkhamApp.Investigators -> {
                        InvestigatorsUi.Page(injector)
                    }

                    ArkhamApp.InvestigatorDetails -> {
                        InvestigatorDetailsUi.Page(
                            injector,
                            InvestigatorId(
                                destination.pathParameters["investigatorId"]!!.single(),
                            )
                        )
                    }

                    ArkhamApp.Products -> {
                        ProductsUi.Page(injector)
                    }

                    ArkhamApp.ProductDetails -> {
                        ProductDetailsUi.Page(
                            injector,
                            ProductId(
                                destination.pathParameters["productId"]!!.single(),
                            )
                        )
                    }

                    ArkhamApp.StaticPage -> {
                        StaticPageUi.Page(
                            injector,
                            destination.pathParameters["slug"]!!.single(),
                        )
                    }

                    ArkhamApp.Tools -> {
                        ToolsListUi.Page(injector)
                    }

                    ArkhamApp.ChaosBagSimulator -> {
                        val scenarioId =
                            destination.queryParameters["scenarioId"]?.singleOrNull()?.let { ScenarioId(it) }
                        ChaosBagSimulatorUi.Page(injector, scenarioId)
                    }

                    ArkhamApp.InvestigatorTracker -> {
                        val investigatorId =
                            destination.queryParameters["investigatorId"]?.singleOrNull()?.let { InvestigatorId(it) }
                        InvestigatorTrackerUi.Page(injector, investigatorId)
                    }

                    ArkhamApp.AboutCampaignLog -> {
                        CampaignLogUi.Page(injector)
                    }

                    ArkhamApp.CreateCampaignLog -> {
                        CampaignLogUi.Page(
                            injector,
                            destination.pathParameters["expansionCode"]!!.single(),
                        )
                    }

                    ArkhamApp.ViewCampaignLog -> {
                        CampaignLogUi.Page(
                            injector,
                            destination.pathParameters["expansionCode"]!!.single(),
                            destination.pathParameters["campaignLogId"]!!.single(),
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

            is MissingDestination -> {
                NavigationErrorUi.Page(injector) {
                    Text("${destination.originalUrl} not found")
                }
            }

            else -> {
                NavigationErrorUi.Page(injector) {
                    Text("An unknown error occurred")
                }
            }
        }
    }
}

