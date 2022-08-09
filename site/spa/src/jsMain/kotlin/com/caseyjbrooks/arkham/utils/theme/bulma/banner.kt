package com.caseyjbrooks.arkham.utils.theme.bulma

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.w3c.dom.HTMLParagraphElement

@Composable
fun Banner(
    content: ContentBuilder<HTMLParagraphElement>,
) {
    Div({ classes("box", "cta") }) {
        P({ classes("has-text-centered") }) { content() }
    }
}
