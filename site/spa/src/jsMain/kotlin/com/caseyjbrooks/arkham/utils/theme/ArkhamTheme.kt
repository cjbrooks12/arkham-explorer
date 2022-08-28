package com.caseyjbrooks.arkham.utils.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.LocalInjector
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.Element

@Composable
fun ArkhamTheme(
    injector: ArkhamInjector,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalInjector provides injector) {
        content()
    }
}

@Composable
fun El(
    tagName: String,
    attrs: AttrBuilderContext<Element>? = null,
    content: ContentBuilder<Element>? = null
) {
    TagElement(
        elementBuilder = object : ElementBuilder<Element> {
            private val el: Element by lazy { document.createElement(tagName) }

            @Suppress("UNCHECKED_CAST")
            override fun create(): Element = el.cloneNode() as Element
        },
        applyAttrs = attrs,
        content = content
    )
}
