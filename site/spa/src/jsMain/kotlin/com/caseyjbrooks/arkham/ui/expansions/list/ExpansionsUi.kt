package com.caseyjbrooks.arkham.ui.expansions.list

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
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Column
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.bulma.Row
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.ExpansionType
import org.jetbrains.compose.web.dom.Text

object ExpansionsUi {

    @Composable
    fun Page(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.expansionsViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: ExpansionsContract.State, postInput: (ExpansionsContract.Inputs) -> Unit) {
        MainLayout(state.layout) { layoutState ->
            Header()
            Body(layoutState)
        }
    }

    @Composable
    fun Header() {
        Hero(
            title = { Text("Expansions") },
            size = BulmaSize.Medium,
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ArkhamApp.Home),
                NavigationRoute("Expansions", null, ArkhamApp.Expansions),
            )
        }
    }

    @Composable
    fun Body(layoutState: MainLayoutState) {
        BulmaSection {
            val regularExpansionsChunks = layoutState
                .expansions
                .filter { it.expansionType !is ExpansionType.ReturnTo }
                .chunked(3)

            regularExpansionsChunks.forEach { expansions ->
                Row("features", "is-centered") {
                    expansions.forEach { expansion ->
                        Column("is-4") {
                            val returnToExpansion = layoutState
                                .expansions
                                .singleOrNull {
                                    it.expansionType is ExpansionType.ReturnTo &&
                                        expansion.code == (it.expansionType as ExpansionType.ReturnTo).forCycle
                                }
                            ExpansionCard(expansion, returnToExpansion)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ExpansionCard(
        expansion: ExpansionLite,
        returnToExpansion: ExpansionLite?,
    ) {
        Card(
            imageUrl = expansion.boxArt,
            title = expansion.name,
            description = expansion.flavorText,
            navigationRoutes = buildList<NavigationRoute> {
                this += NavigationRoute(
                    "Expansion Details",
                    expansion.icon,
                    ArkhamApp.ExpansionDetails,
                    expansion.code,
                )
                if (returnToExpansion != null) {
                    this += NavigationRoute(
                        "Return To...",
                        returnToExpansion.icon,
                        ArkhamApp.ExpansionDetails,
                        expansion.code,
                    )
                }
            }.toTypedArray()
        )
    }
}
