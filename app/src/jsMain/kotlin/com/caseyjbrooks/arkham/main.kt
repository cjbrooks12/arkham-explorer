package com.caseyjbrooks.arkham

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.di.ArkhamInjectorImpl
import com.caseyjbrooks.arkham.ui.MainApplication
import com.caseyjbrooks.arkham.utils.theme.ArkhamTheme
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        val applicationScope = rememberCoroutineScope()
        val injector: ArkhamInjector = remember(applicationScope) { ArkhamInjectorImpl(applicationScope) }

        ArkhamTheme(injector) {
            MainApplication(injector)
        }
    }
}
