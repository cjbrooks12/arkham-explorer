package com.caseyjbrooks.arkham.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.caseyjbrooks.arkham.ui.LocalInjector
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
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
    val httpClient = LocalInjector.current.httpClient
    if (src.endsWith(".svg")) {
        // fetch the SVG and insert it directly into the DOM so we can style it with CSS
        val svgSource: Result<String>? by produceState<Result<String>?>(null) {
            value = runCatching {
                val response = httpClient.get(src)
                check(response.status.isSuccess())
                response.bodyAsText()
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
