package com.caseyjbrooks.arkham.ui.scenarios.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import org.jetbrains.compose.web.dom.Text

object ScenarioDetailsUi {
    @Composable
    fun Content(injector: ArkhamInjector, scenarioId: String) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.scenarioDetailsViewModel(coroutineScope, scenarioId) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: ScenarioDetailsContract.State, postInput: (ScenarioDetailsContract.Inputs)->Unit) {
        Text("Scenario Details: ${state.scenarioId}")
    }
}
