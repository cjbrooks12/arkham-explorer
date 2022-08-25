package com.caseyjbrooks.arkham.ui.tools.campaignlog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.CacheReady
import com.caseyjbrooks.arkham.utils.DynamicGrid
import com.caseyjbrooks.arkham.utils.GridItem
import com.caseyjbrooks.arkham.utils.navigation.Icon
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.ballast.repository.cache.getCachedOrEmptyList
import com.copperleaf.ballast.repository.cache.getValueOrNull
import com.copperleaf.forms.compose.bulma.form.BulmaForm
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

@Suppress("UNUSED_PARAMETER")
object CampaignLogUi {
    @Composable
    fun Page(injector: ArkhamInjector, expansionCode: String? = null, campaignLogId: String? = null) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, expansionCode, campaignLogId) {
            injector.campaignLogViewModel(
                coroutineScope,
                expansionCode,
                campaignLogId,
            )
        }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: CampaignLogContract.State, postInput: (CampaignLogContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            Header(state, postInput)
            Body(state, postInput)
        }
    }

    @Composable
    fun Header(state: CampaignLogContract.State, postInput: (CampaignLogContract.Inputs) -> Unit) {
        Hero(
            title = { Text("Campaign Log") },
            subtitle = { Text("Tools") },
            size = BulmaSize.Small,
        )
        BulmaSection {
            Breadcrumbs(
                *buildList<NavigationRoute> {
                    this += NavigationRoute("Home", null, ArkhamApp.Home)
                    this += NavigationRoute("Tools", null, ArkhamApp.Tools)

                    val expansionValue = state.expansion.getValueOrNull()

                    if (state.expansionCode != null && state.campaignLogId != null) {
                        if (expansionValue != null) {
                            this += NavigationRoute(
                                "${expansionValue.name} Campaign Log",
                                null,
                                ArkhamApp.CreateCampaignLog,
                                state.expansionCode
                            )
                            this += NavigationRoute(
                                "My Campaign",
                                null,
                                ArkhamApp.ViewCampaignLog,
                                state.expansionCode,
                                state.campaignLogId
                            )
                        }
                    } else if (state.expansionCode != null && state.campaignLogId == null) {
                        if (expansionValue != null) {
                            this += NavigationRoute(
                                "New ${expansionValue.name} Campaign",
                                null,
                                ArkhamApp.CreateCampaignLog,
                                state.expansionCode
                            )
                        }
                    } else {
                        this += NavigationRoute("Campaign Log", null, ArkhamApp.AboutCampaignLog)
                    }
                }.toTypedArray()
            )
        }
    }

    @Composable
    fun Body(state: CampaignLogContract.State, postInput: (CampaignLogContract.Inputs) -> Unit) {
        DynamicGrid(
            GridItem("is-4") {
                Card(title = "Investigators") {
                    CacheReady(state.investigatorsFormDefinition) { form ->
                        BulmaForm(
                            schema = form.schema,
                            uiSchema = form.uiSchema,
                            data = state.investigatorFormData,
                            onDataChanged = { postInput(CampaignLogContract.Inputs.InvestigatorsFormDataUpdated(it)) }
                        )
                    }
                }
            },
            GridItem("is-8") {
                Card(title = "Scenarios") {
                    var isDropdownVisible by remember { mutableStateOf(false) }

                    Div({ classes("tabs", "is-boxed") }) {
                        Ul {
                            state.scenarios.forEach { (scenarioLite, cachedScenario) ->
                                Li({
                                    if (scenarioLite.id == state.currentScenarioId) {
                                        classes("is-active")
                                    }
                                    onClick { postInput(CampaignLogContract.Inputs.ChangeSelectedTab(scenarioLite.id)) }
                                }) {
                                    A(null, {}) {
                                        Span({ classes("icon", "is-small"); style { width(auto) } }) {
                                            Icon(scenarioLite.icon)
                                        }
                                        Span {
                                            Text(scenarioLite.name)
                                        }
                                    }
                                }
                            }

                            if (state.availableScenarios.getCachedOrEmptyList().isNotEmpty()) {
                                Li({
                                    onClick { isDropdownVisible = !isDropdownVisible }
                                }) {
                                    A(null, {}) {
                                        Span {
                                            Text("+ Add Scenario")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (isDropdownVisible) {
                        Div({ classes("dropdown", "is-active") }) {
                            Div({ classes("dropdown-menu") }) {
                                Div({ classes("dropdown-content") }) {
                                    CacheReady(state.availableScenarios) { scenarios ->
                                        scenarios.forEach { scenario ->
                                            A(null, {
                                                classes("dropdown-item")
                                                onClick {
                                                    isDropdownVisible = false
                                                    postInput(CampaignLogContract.Inputs.AddScenario(scenario))
                                                }
                                            }) {
                                                Span({ classes("icon", "is-small"); style { width(auto) } }) {
                                                    Icon(scenario.icon)
                                                }
                                                Span {
                                                    Text(scenario.name)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Div {
                        CacheReady(state.scenarioFormDefinition ?: return@Div) { form ->
                            BulmaForm(
                                schema = form.schema,
                                uiSchema = form.uiSchema,
                                data = state.scenarioFormData,
                                onDataChanged = { postInput(CampaignLogContract.Inputs.ScenarioFormDataUpdated(it)) }
                            )
                        }
                    }

//                    Div {
//                        Pre {
//                            Code {
//                                Text(state.allData.toJsonString(true))
//                            }
//                        }
//                    }
                }
            },
        )
    }
}
