package com.caseyjbrooks.arkham.ui.expansions.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.isLoading
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

object ExpansionDetailsUi {
    @Composable
    fun Content(injector: ArkhamInjector, expansionId: String) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.expansionDetailsViewModel(coroutineScope, expansionId) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: ExpansionDetailsContract.State, postInput: (ExpansionDetailsContract.Inputs)->Unit) {
        Text("Expansion Details")

        Div(attrs = { classes("content") }) {
            Ul {
                Li {
                    A(href = "#${ArkhamApp.Home.directions()}") { Text("Home") }
                }
                Li {
                    A(href = "#${ArkhamApp.Expansions.directions()}") { Text("Back") }
                }

                if (state.expansion.isLoading()) {
                    Li { Text("Loading") }
                } else {
                    state.expansion.getCachedOrNull()?.let { expansion ->
                        Li { Span { Text(expansion.name) } }
                    }
                }
            }
        }
    }
}
