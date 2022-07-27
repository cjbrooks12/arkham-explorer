package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerViewModel
import com.caseyjbrooks.arkham.ui.main.RouterViewModel
import kotlinx.coroutines.CoroutineScope

interface ArkhamInjector {
    fun routerViewModel(): RouterViewModel
    fun arkhamExplorerViewModel(coroutineScope: CoroutineScope): ArkhamExplorerViewModel
}
