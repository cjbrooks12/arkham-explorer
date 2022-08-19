package com.caseyjbrooks.arkham.ui.encountersets.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.CacheReady
import com.caseyjbrooks.arkham.utils.DynamicGrid
import com.caseyjbrooks.arkham.utils.GridItem
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.EncounterSetLite
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.ExpansionType
import org.jetbrains.compose.web.dom.Text

@Suppress("UNUSED_PARAMETER")
object EncounterSetsUi {
    @Composable
    fun Page(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.encounterSetsViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: EncounterSetsContract.State, postInput: (EncounterSetsContract.Inputs) -> Unit) {
        MainLayout(state.layout) { layoutState ->
            Header()
            CacheReady(state.encounterSets) { encounterSets ->
                Body(layoutState, encounterSets.encounterSets)
            }
        }
    }

    @Composable
    fun Header() {
        Hero(
            title = { Text("Encounter Sets") },
            size = BulmaSize.Medium,
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ArkhamApp.Home),
                NavigationRoute("Encounter Sets", null, ArkhamApp.EncounterSets),
            )
        }
    }

    @Composable
    fun Body(layoutState: MainLayoutState, encounterSets: List<EncounterSetLite>) {
        DynamicGrid(
            layoutState
                .expansions
                .filter { it.expansionType is ExpansionType.Cycle }
                .map { expansion ->
                    GridItem {
                        ExpansionCard(
                            expansion,
                            encounterSets
                                .filter { it.expansionCode == expansion.expansionCode }
                        )
                    }
                }
        )
    }

    @Composable
    private fun ExpansionCard(expansion: ExpansionLite, encounterSets: List<EncounterSetLite>) {
        Card(
            imageUrl = expansion.boxArt,
            title = expansion.name,
            navigationRoutes = encounterSets
                .map { encounterSet ->
                    NavigationRoute(
                        name = encounterSet.name,
                        iconUrl = encounterSet.icon,
                        route = ArkhamApp.EncounterSetDetails,
                        pathParams = arrayOf(encounterSet.id.id),
                    )
                }
                .toTypedArray()
        )
    }
}
