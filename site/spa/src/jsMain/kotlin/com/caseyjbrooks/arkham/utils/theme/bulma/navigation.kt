package com.caseyjbrooks.arkham.utils.theme.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.caseyjbrooks.arkham.utils.navigation.Icon
import com.caseyjbrooks.arkham.utils.navigation.NavigationLink
import com.copperleaf.ballast.navigation.routing.Route
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

class NavigationSection(
    val name: String,
    vararg val routes: NavigationRoute,
)

class NavigationRoute(
    val name: String,
    val iconUrl: String?,
    val route: Route,
    vararg val params: String,
)

// NavBar
// ---------------------------------------------------------------------------------------------------------------------

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
        Container {
            var mobileNavbarOpen by remember { mutableStateOf(false) }

            Div(attrs = {
                classes("navbar-brand")
            }) {
                NavigationLink(homeRoute, classes = listOf("navbar-item")) {
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
}

@Composable
fun NavbarSection(
    sections: List<NavigationSection>,
) {
    sections.forEach { section ->
        if (section.routes.size == 1) {
            val navigationRoute = section.routes.single()
            NavigationLink(
                route = navigationRoute.route,
                pathParameters = navigationRoute.params,
                classes = listOf("navbar-item"),
            ) {
                if (navigationRoute.iconUrl != null) {
                    Span({ classes("icon", "is-small") }) {
                        Icon(navigationRoute.iconUrl)
                    }
                }
                Text(section.name)
            }
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
                    section.routes.forEach { navigationRoute ->
                        NavigationLink(
                            route = navigationRoute.route,
                            pathParameters = navigationRoute.params,
                            classes = listOf("navbar-item"),
                        ) {
                            if (navigationRoute.iconUrl != null) {
                                Span({
                                    classes("icon", "is-small")
                                }) {
                                    Icon(navigationRoute.iconUrl)
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

// Breadcrumb
// ---------------------------------------------------------------------------------------------------------------------

@Composable
fun Breadcrumbs(
    vararg routes: NavigationRoute,
) {
    Nav({ classes("breadcrumb", "has-succeeds-separator"); attr("aria-label", "breadcrumbs") }) {
        Ul {
            routes.forEachIndexed { index, navigationRoute ->
                if (index == routes.lastIndex) {
                    Li({ classes("is-active") }) {
                        NavigationLink(
                            route = navigationRoute.route,
                            pathParameters = navigationRoute.params,
                            attrs = { attr("aria-current", "page") },
                            classes = listOf("has-text-white")
                        ) {
                            if (navigationRoute.iconUrl != null) {
                                Span({ classes("icon", "is-small"); style { width(auto) } }) {
                                    Icon(navigationRoute.iconUrl) {
                                        classes("has-text-white")
                                    }
                                }
                            }
                            Text(navigationRoute.name)
                        }
                    }
                } else {
                    Li {
                        NavigationLink(
                            route = navigationRoute.route,
                            pathParameters = navigationRoute.params,
                            classes = listOf("has-text-success"),
                        ) {
                            if (navigationRoute.iconUrl != null) {
                                Span({ classes("icon", "is-small"); style { width(auto) } }) {
                                    Icon(navigationRoute.iconUrl) {
                                        classes("has-text-success")
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
