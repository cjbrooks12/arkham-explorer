package com.caseyjbrooks.arkham.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.CacheReady
import com.caseyjbrooks.arkham.utils.theme.bulma.Banner
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
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

object HomeUi {
    @Composable
    fun Content(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.homeViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: HomeContract.State, postInput: (HomeContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            Hero(
                title = { Text("Arkham Explorer") },
                subtitle = { Text("A comprehensive archive for resources, assets, and structured data for Arkham Horror: The Card Game") },
                size = BulmaSize.Medium,
            )
            Banner {
                Span({ classes("tag", "is-primary") }) { Text("New") }
                Text("Fantasy Flight Games has announced a new expansion, The Scarlet Keys!")
            }
            BulmaSection {
                Breadcrumbs(
                    NavigationRoute("Home", null, ArkhamApp.Home),
                )
            }
            BulmaSection {
                CacheReady(state.layout) { layoutState ->
                    val regularExpansionsChunks = layoutState
                        .expansions
                        .filter { !it.isReturnTo }
                        .chunked(3)

                    regularExpansionsChunks.forEach { expansions ->
                        Row("features") {
                            expansions.forEach { expansion ->
                                Column("is-4") {
                                    ExpansionCard(expansion)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ExpansionCard(expansion: ExpansionLite) {
        Card(
            imageUrl = expansion.boxArt,
            title = expansion.name,
            description = expansion.flavorText,
            navigationRoutes = buildList<NavigationRoute> {
                this += NavigationRoute("Expansion Details", null, ArkhamApp.ExpansionDetails, expansion.code)
                if (expansion.hasReturnTo) {
                    this += NavigationRoute("Return To...", null, ArkhamApp.ExpansionDetails, expansion.returnToCode!!)
                }
            }.toTypedArray()
        )
    }

    @Composable
    fun NotFound(path: String) {
        Text("${path} not found")
    }

    @Composable
    fun UnknownError() {
        Text("Unknown error")
    }
}
