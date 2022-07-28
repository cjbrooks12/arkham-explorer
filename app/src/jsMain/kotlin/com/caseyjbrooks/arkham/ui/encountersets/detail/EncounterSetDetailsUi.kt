package com.caseyjbrooks.arkham.ui.encountersets.detail

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

object EncounterSetDetailsUi {
    @Composable
    fun Content(injector: ArkhamInjector, encounterSetId: String) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.encounterSetDetailsViewModel(coroutineScope, encounterSetId) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: EncounterSetDetailsContract.State, postInput: (EncounterSetDetailsContract.Inputs) -> Unit) {
        Text("Encounter Set Details")

        Div(attrs = { classes("content") }) {
            Ul {
                Li {
                    A(href = "#${ArkhamApp.Home.directions()}") { Text("Home") }
                }
                Li {
                    A(href = "#${ArkhamApp.EncounterSets.directions()}") { Text("Back") }
                }

                if (state.encounterSet.isLoading()) {
                    Li { Text("Loading") }
                } else {
                    state.encounterSet.getCachedOrNull()?.let { (expansion, encounterSet) ->
                        Li {
                            val directionsParams = mapOf("expansionId" to listOf(expansion.name))
                            A(href = "#${ArkhamApp.ExpansionDetails.directions(directionsParams)}") {
                                Text(expansion.name)
                            }
                        }
                        Li { Span { Text(encounterSet.name) } }
                    }
                }
            }
        }
    }
}
