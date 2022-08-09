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


/**
 * Use this method to mount the composition at the [HTMLBodyElement] of the current document
 *
 * @param content - the Composable lambda that defines the composition content
 *
 * @return the instance of the [Composition]
 */
fun renderComposableInHead(
    content: @Composable DOMScope<HTMLHeadElement>.() -> Unit
): Composition = renderComposable(
    root = document.getElementsByTagName("head")[0] as HTMLHeadElement,
    content = content
)

private val Title: ElementBuilder<HTMLDivElement> = ElementBuilderImplementation2("title")

private open class ElementBuilderImplementation2<TElement : Element>(private val tagName: String) :
    ElementBuilder<TElement> {
    private val el: Element by lazy { document.createElement(tagName) }

    @Suppress("UNCHECKED_CAST")
    override fun create(): TElement = el.cloneNode() as TElement
}

@Composable
fun Title(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    TagElement(
        elementBuilder = Title,
        applyAttrs = attrs,
        content = content
    )
}

fun browserMain() {
    renderComposableInHead {
        val applicationScope = rememberCoroutineScope()
        val injector: ArkhamInjector = remember(applicationScope) { ArkhamInjectorImpl(applicationScope, false) }

        ArkhamTheme(injector) {

        }
    }

    renderComposableInBody {
        val applicationScope = rememberCoroutineScope()
        val injector: ArkhamInjector = remember(applicationScope) { ArkhamInjectorImpl(applicationScope, false) }

        ArkhamTheme(injector) {
            MainApplication(injector)
        }
    }
}
