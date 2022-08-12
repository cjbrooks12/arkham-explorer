package com.caseyjbrooks.arkham.ui.encountersets.list

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
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Column
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.bulma.Row
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.EncounterSet
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.ExpansionType
import org.jetbrains.compose.web.dom.Text

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
    fun Body(layoutState: MainLayoutState, encounterSets: List<EncounterSet>) {
        BulmaSection {
            val regularExpansionsChunks = layoutState
                .expansions
                .filter { it.expansionType is ExpansionType.Cycle }
                .chunked(3)

            regularExpansionsChunks.forEach { expansions ->
                Row("features", "is-centered") {
                    expansions.forEach { expansion ->
                        Column("is-4") {
                            ExpansionCard(
                                expansion,
                                expansion
                                    .encounterSets
                                    .map { encounterSetId ->
                                        encounterSets.single { it.id == encounterSetId }
                                    },
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ExpansionCard(expansion: ExpansionLite, encounterSets: List<EncounterSet>) {
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
