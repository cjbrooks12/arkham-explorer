package com.caseyjbrooks.arkham

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.di.ArkhamInjectorImpl
import com.caseyjbrooks.arkham.ui.MainApplication
import org.jetbrains.compose.web.renderComposableInBody

fun browserMain(isPwa: Boolean) {
    renderComposableInBody {
        val applicationScope = rememberCoroutineScope()
        val injector: ArkhamInjector = remember(applicationScope, isPwa) { ArkhamInjectorImpl(applicationScope, isPwa) }

        MainApplication(injector)
    }
}
