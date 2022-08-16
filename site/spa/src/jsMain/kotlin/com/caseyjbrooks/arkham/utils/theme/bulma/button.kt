package com.caseyjbrooks.arkham.utils.theme.bulma

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span

object ButtonGroupScope {
    @Composable
    fun ButtonInGroup(
        onClick: () -> Unit,
        content: @Composable () -> Unit
    ) {
        P({ classes("control") }) {
            Button({ classes("button"); onClick { onClick() } }) {
                Span({ classes("icon", "is-small") }) {

                }
                Span({ }) {
                    content()
                }
            }
        }
    }
}

@Composable
fun ButtonGroup(
    content: @Composable ButtonGroupScope.() -> Unit
) {
    Div({ classes("field", "has-addons") }) {
        ButtonGroupScope.content()
    }
}
