package com.caseyjbrooks.arkham.ui.error

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout

object NavigationErrorUi {
    @Composable
    fun Page(injector: ArkhamInjector, content: @Composable () -> Unit) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.navigationErrorViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()

        MainLayout(vmState.layout) {
            content()
        }
    }
}
