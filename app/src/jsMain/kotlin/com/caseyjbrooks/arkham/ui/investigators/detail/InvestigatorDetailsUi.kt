package com.caseyjbrooks.arkham.ui.investigators.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import org.jetbrains.compose.web.dom.Text

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
        Text("Investigator Details: ${state.investigatorId}")
    }
}
