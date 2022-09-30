package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.ui.RouterViewModel

interface RepositoryScope {
    val singletonScope: SingletonScope
    fun routerViewModel(): RouterViewModel
    fun arkhamExplorerRepository(): ArkhamExplorerRepository
}
