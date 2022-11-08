package com.caseyjbrooks.arkham.di

import com.caseyjbrooks.arkham.repository.main.ArkhamExplorerRepository
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.copperleaf.ballast.navigation.Router

interface RepositoryScope {
    val singletonScope: SingletonScope
    fun routerViewModel(): Router<ArkhamApp>
    fun arkhamExplorerRepository(): ArkhamExplorerRepository
}
