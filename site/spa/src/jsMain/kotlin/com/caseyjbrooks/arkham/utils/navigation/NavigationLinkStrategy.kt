package com.caseyjbrooks.arkham.utils.navigation

import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.directions

sealed interface NavigationLinkStrategy {
    fun createHref(
        route: Route,
        pathParameters: Map<String, List<String>> = emptyMap(),
        queryParameters: Map<String, List<String>> = emptyMap(),
    ): String

    fun getDestination(
        route: Route,
        pathParameters: Map<String, List<String>> = emptyMap(),
        queryParameters: Map<String, List<String>> = emptyMap(),
    ): RouterContract.Inputs.GoToDestination
}

object HashNavigationLinkStrategy : NavigationLinkStrategy {
    override fun createHref(
        route: Route,
        pathParameters: Map<String, List<String>>,
        queryParameters: Map<String, List<String>>,
    ): String {
        return "#${route.directions(pathParameters, queryParameters)}"
    }

    override fun getDestination(
        route: Route,
        pathParameters: Map<String, List<String>>,
        queryParameters: Map<String, List<String>>,
    ): RouterContract.Inputs.GoToDestination {
        return RouterContract.Inputs.GoToDestination(route.directions(pathParameters, queryParameters))
    }
}

object HistoryNavigationLinkStrategy : NavigationLinkStrategy {

    override fun createHref(
        route: Route,
        pathParameters: Map<String, List<String>>,
        queryParameters: Map<String, List<String>>,
    ): String {
        return route.directions(pathParameters, queryParameters)
    }

    override fun getDestination(
        route: Route,
        pathParameters: Map<String, List<String>>,
        queryParameters: Map<String, List<String>>,
    ): RouterContract.Inputs.GoToDestination {
        return RouterContract.Inputs.GoToDestination(route.directions(pathParameters, queryParameters))
    }
}
