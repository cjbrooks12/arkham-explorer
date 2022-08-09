package com.caseyjbrooks.arkham.utils.theme.bulma

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Section
import org.w3c.dom.HTMLParagraphElement

enum class BulmaSize(vararg val classes: String) {
    Small("is-small"), Medium("is-medium"), Large("is-large")
}

@Composable
fun Hero(
    title: ContentBuilder<HTMLParagraphElement>,
    subtitle: ContentBuilder<HTMLParagraphElement>? = null,
    size: BulmaSize = BulmaSize.Small,
    classes: List<String> = emptyList(),
) {
    Section({ classes("hero", "is-primary", *size.classes, *classes.toTypedArray()) }) {
        Div({ classes("hero-body") }) {
            Container("has-text-centered") {
                P({ classes("title") }) { title() }
                if (subtitle != null) {
                    P({ classes("subtitle") }) { subtitle() }
                }
            }
        }
    }
}
