package com.caseyjbrooks.arkham.ui.encountersets.detail

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

object EncounterSetDetailsUi {
    @Composable
    fun Content(injector: ArkhamInjector, encounterSetId: String) {
        val coroutineScope = rememberCoroutineScope()
        val vm =
            remember(coroutineScope, injector) { injector.encounterSetDetailsViewModel(coroutineScope, encounterSetId) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: EncounterSetDetailsContract.State, postInput: (EncounterSetDetailsContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            state.encounterSet.getCachedOrNull()?.let { (expansion, encounterSet) ->
                Hero(
                    title = { Text(encounterSet.name) },
                    subtitle = { Text(expansion.name) },
                    size = BulmaSize.Small,
                    classes = listOf("special"),
                )
                BulmaSection {
                    Breadcrumbs(
                        NavigationRoute("Home", null, ArkhamApp.Home),
                        NavigationRoute("Expansions", null, ArkhamApp.Expansions),
                        NavigationRoute(expansion.name, expansion.icon, ArkhamApp.ExpansionDetails, expansion.name),
                        NavigationRoute(encounterSet.name, encounterSet.icon, ArkhamApp.EncounterSetDetails, encounterSet.name),
                    )
                }
            }
        }
    }
}
