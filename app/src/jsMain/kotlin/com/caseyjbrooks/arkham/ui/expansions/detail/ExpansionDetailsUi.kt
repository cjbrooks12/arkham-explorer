package com.caseyjbrooks.arkham.ui.expansions.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import org.jetbrains.compose.web.dom.Text

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
        Text("Expansion Details: ${state.expansionId}")
    }
}
