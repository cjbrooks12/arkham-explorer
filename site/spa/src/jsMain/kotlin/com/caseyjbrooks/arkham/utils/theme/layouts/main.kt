package com.caseyjbrooks.arkham.utils.theme.layouts

import androidx.compose.runtime.Composable
import com.caseyjbrooks.arkham.app.BuildConfig
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.CacheReady
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaFooter
import com.caseyjbrooks.arkham.utils.theme.bulma.MainNavBar
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationSection
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.map
import org.jetbrains.compose.web.dom.Text

data class MainLayoutState(
    val brandImage: String,
    val expansions: List<ExpansionLite>,
    val startNavigation: List<NavigationSection>,
    val endNavigation: List<NavigationSection>,
) {
    companion object {
        fun fromCached(expansions: Cached<ExpansionList>): Cached<MainLayoutState> {
            return expansions.map { fromValue(it) }
        }

        fun fromValue(expansions: ExpansionList): MainLayoutState {
            return MainLayoutState(
                brandImage = "${BuildConfig.BASE_URL}/assets/main-logo.png",
                expansions = expansions.expansions,
                startNavigation = listOf(
                    NavigationSection(
                        "Expansions",
                        NavigationRoute("All Expansions", null, ArkhamApp.Expansions),
                        *expansions
                            .expansions
                            .map { expansion ->
                                NavigationRoute(
                                    expansion.name,
                                    expansion.icon,
                                    ArkhamApp.ExpansionDetails,
                                    expansion.code,
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
                        NavigationRoute("Community Resources", null, ArkhamApp.StaticPage, "resources"),
                        NavigationRoute("Chaos Bag Simulator", null, ArkhamApp.ChaosBagSimulator),
                        NavigationRoute("Campaign Log", null, ArkhamApp.AboutCampaignLog),
                        NavigationRoute("Dividers Generator", null, ArkhamApp.DividersGenerator),
                        NavigationRoute("Tuckbox Generator", null, ArkhamApp.TuckboxGenerator),
                        NavigationRoute("Custom Cards Designer", null, ArkhamApp.CustomCards),
                    ),
                    NavigationSection(
                        "About",
                        NavigationRoute("Resources", null, ArkhamApp.StaticPage, "about"),
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
    content: @Composable (MainLayoutState) -> Unit
) {
    CacheReady(cached) { layoutState ->
        MainNavBar(
            homeRoute = NavigationRoute("Home", null, ArkhamApp.Home),
            brandImageUrl = layoutState.brandImage,
            startNavigation = layoutState.startNavigation,
            endNavigation = layoutState.endNavigation,
        )
        content(layoutState)
        BulmaFooter {
            Text("The information presented on this site about Arkham Horror: The Card Game, both literal and graphical, is copyrighted by Fantasy Flight Games. This website is not produced, endorsed, supported, or affiliated with Fantasy Flight Games.")
        }
    }
}
