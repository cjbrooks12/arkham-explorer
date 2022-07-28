package com.caseyjbrooks.arkham.ui.encountersets.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import org.jetbrains.compose.web.dom.Text

object EncounterSetDetailsUi {
    @Composable
    fun Content(injector: ArkhamInjector, encounterSetId: String) {
        val coroutineScope = rememberCoroutineScope()
        val vm =
            remember(coroutineScope, injector) { injector.encounterSetDetailsViewModel(coroutineScope, encounterSetId) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: EncounterSetDetailsContract.State, postInput: (EncounterSetDetailsContract.Inputs) -> Unit) {
        Text("EncounterSet Details: ${state.encounterSetId}")
    }
}
