package com.caseyjbrooks.arkham.ui.investigators.list

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
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.Investigator
import org.jetbrains.compose.web.dom.Text

object InvestigatorsUi {
    @Composable
    fun Content(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.investigatorsViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: InvestigatorsContract.State, postInput: (InvestigatorsContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            Hero(
                title = { Text("Investigators") },
                size = BulmaSize.Medium,
            )
            BulmaSection {
                Breadcrumbs(
                    NavigationRoute("Home", null, ArkhamApp.Home),
                    NavigationRoute("Investigators", null, ArkhamApp.Investigators),
                )
            }
            BulmaSection {
                CacheReady(state.layout, state.investigators) { layoutState, investigators ->
                    val regularExpansionsChunks = layoutState
                        .expansions
                        .filter { !it.isReturnTo }
                        .chunked(3)

                    regularExpansionsChunks.forEach { expansions ->
                        Row("features") {
                            expansions.forEach { expansion ->
                                Column("is-4") {
                                    val investigators = expansion
                                        .investigators
                                        .map { investigatorId ->
                                            investigators.investigators.single { it.id == investigatorId }
                                        }
                                    ExpansionCard(expansion, investigators)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ExpansionCard(expansion: ExpansionLite, investigators: List<Investigator>) {
        Card(
            imageUrl = expansion.boxArt,
            title = expansion.name,
            navigationRoutes = investigators
                .map { investigator ->
                    NavigationRoute(
                        name = investigator.name,
                        iconUrl = null,
                        route = ArkhamApp.InvestigatorDetails,
                        params = arrayOf(investigator.id.id)
                    )
                }
                .toTypedArray()
        )
    }
}
