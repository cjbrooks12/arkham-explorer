package com.caseyjbrooks.arkham.ui.expansions.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.navigation.Icon
import com.caseyjbrooks.arkham.utils.navigation.NavigationLink
import com.caseyjbrooks.arkham.utils.navigation.NavigationLinkBack
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.isLoading
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
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
    fun Content(state: ExpansionDetailsContract.State, postInput: (ExpansionDetailsContract.Inputs) -> Unit) {
        Text("Expansion Details")

        Div(attrs = { classes("content") }) {
            Ul {
                Li {
                    NavigationLink(ArkhamApp.Home) { Text("Home") }
                }
                Li {
                    NavigationLink(ArkhamApp.Expansions) { Text("Up") }
                }
                Li {
                    NavigationLinkBack { Text("Back") }
                }

                if (state.expansion.isLoading()) {
                    Li { Text("Loading") }
                } else {
                    state.expansion.getCachedOrNull()?.let { expansion ->
                        Li {
                            Icon(src = expansion.icon, alt = expansion.name)
                            Text(expansion.name)
                        }
                        Ul {
                            Li { Text("Scenarios") }
                            Ul {
                                expansion.scenarios.forEach { scenario ->
                                    Li {
                                        NavigationLink(ArkhamApp.ScenarioDetails, scenario.name) {
                                            Icon(src = scenario.icon, alt = scenario.name)
                                            Text(scenario.name)
                                        }
                                    }
                                    Ul {
                                        scenario.encounterSets.forEach { encounterSetRef ->
                                            val encounterSet = state.getEncounterSetByName(encounterSetRef.name)
                                            Li {
                                                NavigationLink(ArkhamApp.EncounterSetDetails, encounterSet.name) {
                                                    Icon(src = encounterSet.icon, alt = encounterSet.name)
                                                    Text(encounterSet.name)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Ul {
                            Li { Text("Encounter Sets") }
                            Ul {
                                expansion.encounterSets.forEach { encounterSet ->
                                    Li {
                                        NavigationLink(ArkhamApp.EncounterSetDetails, encounterSet.name) {
                                            Icon(src = encounterSet.icon, alt = encounterSet.name)
                                            Text(encounterSet.name)
                                        }
                                    }
                                }
                            }
                        }
                        Ul {
                            Li { Text("Investigators") }
                            Ul {
                                expansion.investigators.forEach { investigator ->
                                    Li {
                                        NavigationLink(ArkhamApp.InvestigatorDetails, investigator.name) {
                                            Text(investigator.name)
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
}
