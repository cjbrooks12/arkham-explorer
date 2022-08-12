package com.caseyjbrooks.arkham.ui.investigators.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.CacheReady
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.Investigator
import com.copperleaf.arkham.models.api.InvestigatorId
import org.jetbrains.compose.web.dom.Text

object InvestigatorDetailsUi {
    @Composable
    fun Page(injector: ArkhamInjector, investigatorId: InvestigatorId) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, investigatorId) {
            injector.investigatorDetailsViewModel(
                coroutineScope,
                investigatorId
            )
        }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: InvestigatorDetailsContract.State, postInput: (InvestigatorDetailsContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            CacheReady(
                state.parentExpansion,
                state.investigator,
            ) { expansion, investigator ->
                Header(expansion, investigator)
                Body()
            }
        }
    }

    @Composable
    fun Header(expansion: ExpansionLite, investigator: Investigator) {
        Hero(
            title = { Text(investigator.name) },
            subtitle = { Text(expansion.name) },
            size = BulmaSize.Small,
            classes = listOf("special"),
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ArkhamApp.Home),
                NavigationRoute("Expansions", null, ArkhamApp.Expansions),
                NavigationRoute(expansion.name, expansion.icon, ArkhamApp.ExpansionDetails, expansion.code),
                NavigationRoute(investigator.name, null, ArkhamApp.InvestigatorDetails, investigator.id.id),
            )
        }
    }

    @Composable
    fun Body() {

    }
}
