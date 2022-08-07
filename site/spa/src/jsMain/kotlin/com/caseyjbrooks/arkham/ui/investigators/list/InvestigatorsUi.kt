package com.caseyjbrooks.arkham.ui.investigators.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.navigation.NavigationLink
import com.caseyjbrooks.arkham.utils.navigation.NavigationLinkBack
import com.copperleaf.ballast.repository.cache.getCachedOrEmptyList
import com.copperleaf.ballast.repository.cache.isLoading
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

object InvestigatorsUi {
    @Composable
    fun Content(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.investigatorsViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: InvestigatorsContract.State, postInput: (InvestigatorsContract.Inputs) -> Unit) {
        Text("Investigators")

        Div(attrs = { classes("content") }) {
            Ul {
                Li {
                    NavigationLink(ArkhamApp.Home) { Text("Home") }
                }
                Li {
                    NavigationLinkBack { Text("Back") }
                }

                if (state.expansions.isLoading()) {
                    Li { Text("Loading") }
                } else {
                    state.expansions.getCachedOrEmptyList().forEach { expansion ->
                        Li { Span { Text(expansion.name) } }
//                        Ul {
//                            expansion.investigators.forEach { investigator ->
//                                Li { NavigationLink(ArkhamApp.InvestigatorDetails, investigator.name) { Text(investigator.name) } }
//                            }
//                        }
                    }
                }
            }
        }
    }
}
