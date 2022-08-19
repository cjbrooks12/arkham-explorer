package com.caseyjbrooks.arkham.ui.tools.dividers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.ui.home.HomeContract
import com.caseyjbrooks.arkham.utils.DynamicGrid
import com.caseyjbrooks.arkham.utils.GridItem
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import org.jetbrains.compose.web.dom.Text

@Suppress("UNUSED_PARAMETER")
object DividersGeneratorUi {
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
            Body()
        }
    }

    @Composable
    fun Header() {
        Hero(
            title = { Text("Dividers Generator") },
            subtitle = { Text("Tools") },
            size = BulmaSize.Small,
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ArkhamApp.Home),
                NavigationRoute("Tools", null, ArkhamApp.Tools),
                NavigationRoute("Dividers Generator", null, ArkhamApp.DividersGenerator),
            )
        }
    }

    @Composable
    fun Body() {
        DynamicGrid(
            GridItem {
                Card(title = "Dividers")
            },
            GridItem {
                Card(title = "Divider Options")
            },
            GridItem {
                Card(title = "Preview")
            },
        )
    }
}
