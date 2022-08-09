package com.caseyjbrooks.arkham.utils.theme.bulma

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Footer
import org.jetbrains.compose.web.dom.P
import org.w3c.dom.HTMLParagraphElement


@Composable
fun BulmaFooter(
    content: ContentBuilder<HTMLParagraphElement>? = null
) {
    Footer({ classes("footer") }) {
        Container("has-text-centered", "content") {
            P(content = content)
        }
    }
}
