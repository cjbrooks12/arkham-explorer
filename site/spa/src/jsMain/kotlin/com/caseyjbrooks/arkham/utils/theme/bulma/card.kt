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
import org.jetbrains.compose.web.css.marginBottom
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
    title: String? = null,
    imageUrl: String? = null,
    description: String? = null,
    vararg navigationRoutes: NavigationRoute,
    content: ContentBuilder<HTMLDivElement>? = null,
) {
    Div({ classes("card", "is-shady") }) {
        if (imageUrl != null) {
            Div({ classes("card-image") }) {
                El("figure", { classes("image", "is-square") }) {
                    Img(
                        src = imageUrl,
                        alt = title ?: "",
                        attrs = { classes("modal-button"); style { property("object-fit", "cover") } }
                    )
                }
            }
        }
        Div({ classes("card-content") }) {
            Div({ classes("content") }) {
                if (title != null) {
                    H4({ style { fontFamily("Teutonic") } }) { Text(title) }
                }
                if (description != null) {
                    P({ style { fontFamily("Bolton"); fontSize(1.25.em) } }) { Text(description) }
                }
                if (content != null) {
                    content()
                }

                navigationRoutes.forEachIndexed { index, navigationRoute ->
                    Div({
                        if (index != navigationRoutes.lastIndex) {
                            style { marginBottom(1.cssRem) }
                        }
                    }) {
                        NavigationLink(navigationRoute) {
                            Span({ classes("button", navigationRoute.buttonColor.classes, "modal-button"); style { fontFamily("Teutonic") } }) {
                                if (navigationRoute.iconUrl != null) {
                                    Span({
                                        classes("icon", "is-small")
                                        style {
                                            width(auto)
                                            margin(
                                                0.cssRem,
                                                1.cssRem,
                                                0.cssRem,
                                                0.cssRem
                                            )
                                        }
                                    }) {
                                        Icon(navigationRoute.iconUrl) {
                                            classes("has-text-white")
                                        }
                                    }
                                }
                                Text(navigationRoute.name)
                            }
                        }
                    }
                }
            }
        }
    }
}
