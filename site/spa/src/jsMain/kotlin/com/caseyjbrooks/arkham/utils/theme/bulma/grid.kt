package com.caseyjbrooks.arkham.utils.theme.bulma

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Section
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Composable
fun BulmaSection(
    vararg classes: String,
    content: ContentBuilder<HTMLElement>,
) {
    Section({ classes("section", *classes) }) {
        Container {
            content()
        }
    }
}

@Composable
fun Container(
    vararg classes: String,
    content: ContentBuilder<HTMLElement>,
) {
    Div({ classes("container", *classes) }) {
        content()
    }
}

@Composable
fun Row(
    vararg classes: String,
    content: ContentBuilder<HTMLDivElement>,
) {
    Div({ classes("columns", *classes) }) {
        content()
    }
}

@Composable
fun Column(
    vararg classes: String,
    content: ContentBuilder<HTMLElement>,
) {
    Div({ classes("column", *classes) }) {
        content()
    }
}

@Composable
fun Box(
    vararg classes: String,
    content: ContentBuilder<HTMLDivElement>,
) {
    Div({ classes("box", *classes); style { borderRadius(0.px) } }) {
        content()
    }
}


@Composable
fun Content(
    vararg classes: String,
    content: ContentBuilder<HTMLDivElement>,
) {
    Div({ classes("content", *classes) }) {
        content()
    }
}
