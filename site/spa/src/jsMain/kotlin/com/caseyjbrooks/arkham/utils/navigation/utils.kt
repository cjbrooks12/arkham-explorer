package com.caseyjbrooks.arkham.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.caseyjbrooks.arkham.ui.LocalInjector
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.copperleaf.ballast.navigation.routing.route.PathSegment
import com.copperleaf.ballast.navigation.vm.RouterContract
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLAnchorElement

@Composable
fun NavigationLink(
    navigationRoute: NavigationRoute,
    attrs: AttrBuilderContext<HTMLAnchorElement>? = null,
    onClicked: () -> Unit = { },
    content: @Composable () -> Unit
) {
    val injector = LocalInjector.current
    val route = navigationRoute.route
    val router = remember(injector) { injector.routerViewModel() }
    val href = remember(injector, navigationRoute, route) {
        val parameterPiecesInRoute = route.matcher.path.filterIsInstance<PathSegment.Parameter>()

        check(navigationRoute.pathParams.size == parameterPiecesInRoute.size) {
            "Must have exactly ${parameterPiecesInRoute.size} path parameters to create link for route '$route', had ${navigationRoute.pathParams.size} (${navigationRoute.pathParams})"
        }

        val pathParams = parameterPiecesInRoute
            .mapIndexed { index, piece ->
                piece.name to listOf(navigationRoute.pathParams[index])
            }
            .toMap()

        injector.navigationLinkStrategy.createHref(
            route = route,
            pathParameters = pathParams,
            queryParameters = navigationRoute.queryParams,
        )
    }
    val destination = remember(injector, navigationRoute, route) {
        val parameterPiecesInRoute = route.matcher.path.filterIsInstance<PathSegment.Parameter>()

        check(navigationRoute.pathParams.size == parameterPiecesInRoute.size) {
            "Must have exactly ${parameterPiecesInRoute.size} path parameters to create link for route '$route', had ${navigationRoute.pathParams.size} (${navigationRoute.pathParams})"
        }

        val pathParams = parameterPiecesInRoute
            .mapIndexed { index, piece ->
                piece.name to listOf(navigationRoute.pathParams[index])
            }
            .toMap()

        injector.navigationLinkStrategy.getDestination(
            route = route,
            pathParameters = pathParams,
            queryParameters = navigationRoute.queryParams,
        )
    }

    A(
        href = "${injector.config.baseUrl.trimEnd('/')}$href",
        attrs = {
            onClick {
                if (it.ctrlKey || it.metaKey) {
                    // let it propagate normally, don't handle it with the router
                } else {
                    it.preventDefault()
                    it.stopImmediatePropagation()
                    it.stopPropagation()
                    router.trySend(destination)
                }
                onClicked()
            }
            attrs?.invoke(this)
        }
    ) {
        content()
    }
}

@Composable
fun NavigationLinkBack(
    content: @Composable () -> Unit
) {
    val injector = LocalInjector.current
    val router = remember(injector) { injector.routerViewModel() }
    A(attrs = {
        onClick {
            it.preventDefault()
            it.stopPropagation()
            router.trySend(RouterContract.Inputs.GoBack())
        }
    }) { content() }
}
