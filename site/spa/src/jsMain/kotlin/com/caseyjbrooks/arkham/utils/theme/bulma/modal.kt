package com.caseyjbrooks.arkham.utils.theme.bulma

import androidx.compose.runtime.Composable
import com.caseyjbrooks.arkham.utils.theme.El
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


@Composable
fun Card(
    imageUrl: String,
    title: String,
    description: String,
) {
    Div({ classes("card", "is-shady") }) {
        Div({ classes("card-image") }) {
            El("figure", { classes("image", "is-square") }) {
                Img(
                    src = imageUrl,
                    alt = title,
                    attrs = { classes("modal-button") }
                )
            }
        }
        Div({ classes("card-content") }) {
            Div({ classes("content") }) {
                H4({ style { fontFamily("Teutonic") } }) { Text(title) }
                P({ style { fontFamily("Bolton"); fontSize(1.25.em) } }) { Text(description) }
                Span({ classes("button", "is-link", "modal-button") }) {
                    Text("Details")
                }
            }
        }
    }
}
