package com.caseyjbrooks.arkham.utils.theme.layouts

import androidx.compose.runtime.Composable
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaFooter
import com.caseyjbrooks.arkham.utils.theme.bulma.MainNavBar
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationSection
import com.copperleaf.arkham.models.ArkhamHorrorExpansion
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.ballast.repository.cache.isLoading
import com.copperleaf.ballast.repository.cache.map
import org.jetbrains.compose.web.dom.Text

data class MainLayoutState(
    val brandImage: String,
    val expansions: List<ArkhamHorrorExpansion>,
    val startNavigation: List<NavigationSection>,
    val endNavigation: List<NavigationSection>,
) {
    companion object {
        fun fromCached(expansions: Cached<List<ArkhamHorrorExpansion>>): Cached<MainLayoutState> {
            return expansions.map { fromValue(it) }
        }

        fun fromValue(expansions: List<ArkhamHorrorExpansion>): MainLayoutState {
            return MainLayoutState(
                brandImage = "http://arkhamcentral.com/wp-content/uploads/2017/05/ArkhamHorrorlogo.png",
                expansions = expansions,
                startNavigation = listOf(
                    NavigationSection(
                        "Expansions",
                        NavigationRoute("All Expansions", null, ArkhamApp.Expansions),
                        *expansions
                            .map { expansion ->
                                NavigationRoute(
                                    expansion.name,
                                    expansion.icon,
                                    ArkhamApp.ExpansionDetails,
                                    expansion.name
                                )
                            }
                            .toTypedArray()
                    ),
                    NavigationSection(
                        "Scenarios",
                        NavigationRoute("All Scenarios", null, ArkhamApp.Scenarios)
                    ),
                    NavigationSection(
                        "Encounter Sets",
                        NavigationRoute("All Encounter Sets", null, ArkhamApp.EncounterSets)
                    ),
                    NavigationSection(
                        "Investigators",
                        NavigationRoute("All Investigators", null, ArkhamApp.Investigators),
                    ),
                ),
                endNavigation = listOf(
                    NavigationSection(
                        "Resources",
                        NavigationRoute("Resources", null, ArkhamApp.StaticPage, "resources"),
                    ),
                    NavigationSection(
                        "API",
                        NavigationRoute("API", null, ArkhamApp.StaticPage, "api"),
                    ),
                )
            )
        }
    }
}

@Composable
fun MainLayout(
    cached: Cached<MainLayoutState>,
    content: @Composable () -> Unit
) {
    if (cached.isLoading()) {
        // show a loader

    } else if (cached is Cached.FetchingFailed<*>) {
        // show an error message

    } else {
        val state = cached.getCachedOrThrow()
        MainNavBar(
            homeRoute = ArkhamApp.Home,
            brandImageUrl = state.brandImage,
            startNavigation = state.startNavigation,
            endNavigation = state.endNavigation,
        )
        content()
        BulmaFooter {
            Text("The information presented on this site about Arkham Horror: The Card Game, both literal and graphical, is copyrighted by Fantasy Flight Games. This website is not produced, endorsed, supported, or affiliated with Fantasy Flight Games.")
        }
    }
}
