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
import com.copperleaf.arkham.models.api.EncounterSet
import com.copperleaf.arkham.models.api.ExpansionLite
import org.jetbrains.compose.web.dom.Text

object EncounterSetsUi {
    @Composable
    fun Content(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.encounterSetsViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: EncounterSetsContract.State, postInput: (EncounterSetsContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
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
            BulmaSection {
                CacheReady(state.layout, state.encounterSets) { layoutState, encounterSets ->
                    val regularExpansionsChunks = layoutState
                        .expansions
                        .filter { !it.isReturnTo }
                        .chunked(3)

                    regularExpansionsChunks.forEach { expansions ->
                        Row("features") {
                            expansions.forEach { expansion ->
                                Column("is-4") {
                                    val encounterSets = expansion
                                        .encounterSets
                                        .map { encounterSetId ->
                                            encounterSets.encounterSets.single { it.id == encounterSetId }
                                        }
                                    ExpansionCard(expansion, encounterSets)
                                }
                            }
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
                        params = arrayOf(encounterSet.id.id),
                    )
                }
                .toTypedArray()
        )
    }
}
