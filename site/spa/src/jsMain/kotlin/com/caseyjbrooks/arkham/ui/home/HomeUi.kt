package com.caseyjbrooks.arkham.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.ui.expansions.list.ExpansionsUi
import com.caseyjbrooks.arkham.utils.CacheReady
import com.caseyjbrooks.arkham.utils.theme.bulma.Banner
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Suppress("UNUSED_PARAMETER")
object HomeUi {
    @Composable
    fun Page(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.homeViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: HomeContract.State, postInput: (HomeContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            Header()
            CacheReady(state.layout) { layoutState ->
                // TODO: only show this main list in normal web mode. in PWA, display an app-like dashboard with
                //  shortcuts the end-user can customize
                ExpansionsUi.Body(layoutState)
            }
        }
    }

    @Composable
    fun Header() {
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
    }

    @Composable
    fun Body() {

    }
}
