package com.caseyjbrooks.arkham.ui.home

import androidx.compose.runtime.Composable
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.copperleaf.ballast.navigation.routing.directions
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

object HomeUi {
    @Composable
    fun Content() {

        Div(attrs = { classes("content") }) {
            Ul {
                listOf(
                    ArkhamApp.Home to "Home",
                    ArkhamApp.Expansions to "Expansions",
                    ArkhamApp.Investigators to "Investigators",
                    ArkhamApp.Scenarios to "Scenarios",
                    ArkhamApp.EncounterSets to "Encounter Sets",
                ).forEach { (route, name) ->
                    Li { A(href = "#${route.directions()}") { Text(name) } }
                }
            }
        }
    }

    @Composable
    fun NotFound(path: String) {
        Text("${path} not found")
    }

    @Composable
    fun UnknownError() {
        Text("Unknown error")
    }
}
