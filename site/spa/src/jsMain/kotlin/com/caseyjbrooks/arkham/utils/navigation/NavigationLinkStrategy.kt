package com.caseyjbrooks.arkham.utils.navigation

import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.directions

sealed interface NavigationLinkStrategy {
    fun createLink(
        route: Route,
        pathParameters: Map<String, List<String>> = emptyMap(),
        queryParameters: Map<String, List<String>> = emptyMap(),
    ): String
}

object HashNavigationLinkStrategy : NavigationLinkStrategy {
    override fun createLink(
        route: Route,
        pathParameters: Map<String, List<String>>,
        queryParameters: Map<String, List<String>>,
    ): String {
        return "#${route.directions(pathParameters, queryParameters)}"
    }
}

object HistoryNavigationLinkStrategy : NavigationLinkStrategy {

    override fun createLink(
        route: Route,
        pathParameters: Map<String, List<String>>,
        queryParameters: Map<String, List<String>>,
    ): String {
        return route.directions(pathParameters, queryParameters)
    }
}
