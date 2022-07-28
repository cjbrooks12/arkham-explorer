package com.caseyjbrooks.arkham.ui.investigators.detail

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

object InvestigatorDetailsUi {
    @Composable
    fun Content(injector: ArkhamInjector, investigatorId: String) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.investigatorDetailsViewModel(coroutineScope, investigatorId) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: InvestigatorDetailsContract.State, postInput: (InvestigatorDetailsContract.Inputs)->Unit) {
        Text("Investigator Details")

        Div(attrs = { classes("content") }) {
            Ul {
                Li {
                    A(href = "#${ArkhamApp.Home.directions()}") { Text("Home") }
                }
                Li {
                    A(href = "#${ArkhamApp.Investigators.directions()}") { Text("Back") }
                }

                if (state.investigator.isLoading()) {
                    Li { Text("Loading") }
                } else {
                    state.investigator.getCachedOrNull()?.let { (expansion, investigator) ->
                        Li {
                            val directionsParams = mapOf("expansionId" to listOf(expansion.name))
                            A(href = "#${ArkhamApp.ExpansionDetails.directions(directionsParams)}") {
                                Text(expansion.name)
                            }
                        }
                        Li { Span { Text(investigator.name) } }
                    }
                }
            }
        }
    }
}
