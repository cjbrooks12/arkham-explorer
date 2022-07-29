package com.caseyjbrooks.arkham.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.copperleaf.ballast.navigation.routing.directions
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

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
        Div(attrs = { classes("content") }) {
            Ul {
                listOf(
                    ArkhamApp.Home to "Home",
                    ArkhamApp.Expansions to "Expansions",
                    ArkhamApp.Investigators to "Investigators",
                    ArkhamApp.Scenarios to "Scenarios",
                    ArkhamApp.EncounterSets to "Encounter Sets",
                ).forEach { (route, name) ->
                    Li { A(href = "#${route.directions()}") { Text(name) } }
                }
            }
        }
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
