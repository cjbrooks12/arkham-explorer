package com.caseyjbrooks.arkham.ui.tools.campaignlog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.DynamicGrid
import com.caseyjbrooks.arkham.utils.GridItem
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.ballast.repository.cache.getValueOrNull
import org.jetbrains.compose.web.dom.Text

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
        MainLayout(state.layout) { layoutState ->
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
                        if(expansionValue != null) {
                            this += NavigationRoute("${expansionValue.name} Campaign Log", null, ArkhamApp.CreateCampaignLog, state.expansionCode)
                            this += NavigationRoute("My Campaign", null, ArkhamApp.ViewCampaignLog, state.expansionCode, state.campaignLogId)
                        }
                    } else if (state.expansionCode != null && state.campaignLogId == null) {
                        if(expansionValue != null) {
                            this += NavigationRoute("New ${expansionValue.name} Campaign", null, ArkhamApp.CreateCampaignLog, state.expansionCode)
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
            GridItem {
                Card(title = "Configuration") {

                }
            },
            GridItem {
                Card(title = "Campaign Log") {

                }
            },
        )
    }
}
