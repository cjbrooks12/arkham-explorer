package com.caseyjbrooks.arkham.models

import com.copperleaf.ballast.navigation.routing.Route

class NavigationRoute(
    val name: String,
    val route: Route,
    vararg val params: String,
)
