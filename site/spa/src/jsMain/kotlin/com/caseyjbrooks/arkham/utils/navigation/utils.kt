package com.caseyjbrooks.arkham.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.caseyjbrooks.arkham.ui.LocalInjector
import com.copperleaf.ballast.navigation.routing.PathSegment
import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouterContract
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLAnchorElement

fun NavigationLinkStrategy.createLink(
    route: Route,
    vararg pathParameters: String,
): String {
    val parameterPiecesInRoute = route.matcher.path.filterIsInstance<PathSegment.Parameter>()

    check(pathParameters.size == parameterPiecesInRoute.size) {
        "Must have exactly ${parameterPiecesInRoute.size} path parameters to create link for route '${route.originalRoute}', had ${pathParameters.size} ($pathParameters)"
    }

    val pathParams = parameterPiecesInRoute
        .mapIndexed { index, piece ->
            piece.name to listOf(pathParameters[index])
        }
        .toMap()

    return createLink(route, pathParams)
}

@Composable
fun NavigationLink(
    route: Route,
    vararg pathParameters: String,
    classes: List<String> = emptyList(),
    attrs: AttrBuilderContext<HTMLAnchorElement>? = null,
    content: @Composable () -> Unit
) {
    val injector = LocalInjector.current
    val router = remember(injector) { injector.routerViewModel() }
    val navLink = remember(injector) { injector.navigationLinkStrategy.createLink(route, *pathParameters) }

    A(
        href = "${injector.config.baseUrl.trimEnd('/')}$navLink",
        attrs = {
            onClick {
                if (it.ctrlKey || it.metaKey) {
                    // let it propagate normally, don't handle it with the router
                } else {
                    it.preventDefault()
                    it.stopPropagation()
                    router.trySend(RouterContract.Inputs.GoToDestination(navLink))
                }
            }
            classes(classes)
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
