package com.caseyjbrooks.arkham.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img

@Composable
fun Icon(
    src: String,
    alt: String = "",
    attrs: AttrBuilderContext<*>? = null
) {
    if (src.endsWith(".svg")) {
        // fetch the SVG and insert it directly into the DOM so we can style it with CSS
        val svgSource: Result<String>? by produceState<Result<String>?>(null) {
            val svgResponse = window.fetch(src).await()

            value = runCatching {
                check(svgResponse.ok)
                svgResponse.text().await()
            }
        }

        Div(attrs = {
            svgSource?.getOrNull()?.let { svgText ->
                ref { element ->
                    element.innerHTML = svgText
                    onDispose {}
                }
            }
            style { height(24.px) }
            classes("dynamic-svg")
            attrs?.invoke(this)
        })
    } else {
        // otherwise, display it as-is with a standard <img> tag
        Img(
            src = src,
            alt = alt,
            attrs = {
                style { height(24.px) }
                attrs?.invoke(this)
            }
        )
    }
}
