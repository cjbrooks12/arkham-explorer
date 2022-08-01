package com.caseyjbrooks.arkham.utils.navigation

import androidx.compose.runtime.Composable
import com.caseyjbrooks.arkham.ui.LocalInjector
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Img
import org.w3c.dom.HTMLImageElement

@Composable
fun Icon(
    src: String,
    alt: String = "",
    attrs: AttrBuilderContext<HTMLImageElement>? = null
) {
    val config = LocalInjector.current.config
    Img(
        src = "${config.baseUrl}$src",
        alt = alt,
        attrs = {
            style { height(24.px) }
            attrs?.invoke(this)
        }
    )
}
