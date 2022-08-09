package com.caseyjbrooks.arkham.ui.investigators.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import org.jetbrains.compose.web.dom.Text

object InvestigatorDetailsUi {
    @Composable
    fun Content(injector: ArkhamInjector, investigatorId: String) {
        val coroutineScope = rememberCoroutineScope()
        val vm =
            remember(coroutineScope, injector, investigatorId) { injector.investigatorDetailsViewModel(coroutineScope, investigatorId) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: InvestigatorDetailsContract.State, postInput: (InvestigatorDetailsContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            state.investigator.getCachedOrNull()?.let { (expansion, investigator) ->
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
                        NavigationRoute(expansion.name, expansion.icon, ArkhamApp.ExpansionDetails, expansion.name),
                        NavigationRoute(investigator.name, investigator.icon, ArkhamApp.InvestigatorDetails, investigator.name),
                    )
                }
            }
        }
    }
}
