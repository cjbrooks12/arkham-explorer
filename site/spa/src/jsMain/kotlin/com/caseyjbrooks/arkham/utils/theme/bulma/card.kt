package com.caseyjbrooks.arkham.utils.theme.bulma

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Composable
fun Card(
    content: ContentBuilder<HTMLDivElement>,
) {
    Div({ classes("card") }) {
        Div({ classes("card-content") }) {
            Content {
                content()
            }
        }
    }
}
