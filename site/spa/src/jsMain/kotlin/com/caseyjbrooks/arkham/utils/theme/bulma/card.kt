package com.caseyjbrooks.arkham.utils.theme.bulma

import androidx.compose.runtime.Composable
import com.caseyjbrooks.arkham.utils.navigation.Icon
import com.caseyjbrooks.arkham.utils.navigation.NavigationLink
import com.caseyjbrooks.arkham.utils.theme.El
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
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

@Composable
fun Panel(
    title: String,
    vararg routes: NavigationRoute,
) {
    Div({ classes("card", "is-shady") }) {
        Div({ classes("card-content") }) {
            Div({ classes("content") }) {
                H4({ }) { Text(title) }
                P {
                    Div({ classes("tags", "are-large") }) {
                        routes.forEach { navigationRoute ->
                            NavigationLink(
                                route = navigationRoute.route,
                                pathParameters = navigationRoute.params,
                                classes = listOf("tag", "is-primary")
                            ) {
                                if (navigationRoute.iconUrl != null) {
                                    Span({ classes("icon", "is-small"); style { width(auto); margin(0.cssRem, 1.cssRem, 0.cssRem, 0.cssRem) } }) {
                                        Icon(navigationRoute.iconUrl) {
                                            classes("has-text-white")
                                        }
                                    }
                                }
                                Span({ classes("ml-3"); style { fontFamily("Teutonic") } }) {
                                    Text(navigationRoute.name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
