package com.caseyjbrooks.arkham.utils.navigation

import androidx.compose.runtime.Composable
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
    Img(
        src = src,
        alt = alt,
        attrs = {
            style { height(24.px) }
            attrs?.invoke(this)
        }
    )
}
