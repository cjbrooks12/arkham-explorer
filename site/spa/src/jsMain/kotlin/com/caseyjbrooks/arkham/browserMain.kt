package com.caseyjbrooks.arkham

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.di.ArkhamInjectorImpl
import com.caseyjbrooks.arkham.ui.MainApplication
import com.caseyjbrooks.arkham.utils.theme.ArkhamTheme
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.DOMScope
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.renderComposable
import org.jetbrains.compose.web.renderComposableInBody
import org.w3c.dom.Element
import org.w3c.dom.HTMLBodyElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLHeadElement
import org.w3c.dom.get

fun browserMain(isPwa: Boolean) {
    renderComposableInBody {
        val applicationScope = rememberCoroutineScope()
        val injector: ArkhamInjector = remember(applicationScope, isPwa) { ArkhamInjectorImpl(applicationScope, isPwa) }

        ArkhamTheme(injector) {
            MainApplication(injector)
        }
    }
}
