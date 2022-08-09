package com.caseyjbrooks.arkham.ui.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.caseyjbrooks.arkham.models.NavigationSection
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.navigation.NavigationLink
import com.copperleaf.ballast.navigation.routing.Route
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun MainNavBar(
    homeRoute: Route,
    brandImageUrl: String,
    startNavigation: List<NavigationSection>,
    endNavigation: List<NavigationSection>
) {
    Nav(attrs = {
        classes("navbar", "is-primary")
        attr("role", "navigation")
        attr("aria-label", "main navigation")
    }) {
        var mobileNavbarOpen by remember { mutableStateOf(false) }

        Div(attrs = {
            classes("navbar-brand")
        }) {
            NavigationLink(ArkhamApp.Home, classes = listOf("navbar-item")) {
                Img(
                    attrs = {
                        attr("height", "28")
                    },
                    src = brandImageUrl
                )
            }

            A(
                attrs = {
                    if (mobileNavbarOpen) {
                        classes("navbar-burger", "is-active")
                    } else {
                        classes("navbar-burger")
                    }
                    attr("role", "navigation")
                    attr("aria-label", "menu")
                    attr("aria-expanded", "false")
                    attr("data-target", "navbarMain")
                    onClick { mobileNavbarOpen = !mobileNavbarOpen }
                }
            ) {
                Span(attrs = { attr("aria-hidden", "true") })
                Span(attrs = { attr("aria-hidden", "true") })
                Span(attrs = { attr("aria-hidden", "true") })
            }
        }

        Div(attrs = {
            if (mobileNavbarOpen) {
                classes("navbar-menu", "is-active")
            } else {
                classes("navbar-menu")
            }
            id("navbarMain")
        }) {
            Div(attrs = {
                classes("navbar-start")
            }) {
                NavbarSection(startNavigation)
            }
            Div(attrs = {
                classes("navbar-end")
            }) {
                NavbarSection(endNavigation)
            }
        }
    }
}

@Composable
fun NavbarSection(
    sections: List<NavigationSection>,
) {
    sections.forEach { section ->
        if (section.routes.size == 1) {
            NavigationLink(
                route = section.routes.single().route,
                pathParameters = section.routes.single().params,
                classes = listOf("navbar-item"),
            ) { Text(section.name) }
        } else {
            var isExpanded by remember { mutableStateOf(false) }
            Div({
                if (isExpanded) {
                    classes("navbar-item", "has-dropdown", "is-active")
                } else {
                    classes("navbar-item", "has-dropdown")
                }
            }) {
                A(attrs = {
                    classes("navbar-link")
                    onClick { isExpanded = !isExpanded }
                }) {
                    Text(section.name)
                }

                Div({ classes("navbar-dropdown") }) {
                    section.routes.forEach {
                        NavigationLink(it.route, pathParameters = it.params, classes = listOf("navbar-item")) {
                            Text(it.name)
                        }
                    }
                }
            }
        }
    }
}
