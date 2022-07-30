package com.caseyjbrooks.arkham.utils.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.LocalInjector

@Composable
fun ArkhamTheme(
    injector: ArkhamInjector,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalInjector provides injector) {
        content()
    }
}
