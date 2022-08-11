package com.caseyjbrooks.arkham.ui.expansions.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.CacheReady
import com.caseyjbrooks.arkham.utils.navigation.NavigationLink
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

object ExpansionsUi {

    @Composable
    fun Content(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.expansionsViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: ExpansionsContract.State, postInput: (ExpansionsContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
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
            BulmaSection {
                Card {
                    CacheReady(state.layout) { layout ->
                        Ul {
                            layout.expansions.forEach { expansion ->
                                Li {
                                    NavigationLink(
                                        ArkhamApp.ExpansionDetails,
                                        expansion.code,
                                    ) {
                                        Text(expansion.name)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
