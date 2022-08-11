package com.caseyjbrooks.arkham.ui.chaosbag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.arkham.models.api.ScenarioId
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.name
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text

object ChaosBagSimulatorUi {
    @Composable
    fun Content(injector: ArkhamInjector, scenarioId: ScenarioId?) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, scenarioId) {
            injector.chaosBagSimulatorViewModel(
                coroutineScope,
                scenarioId
            )
        }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: ChaosBagSimulatorContract.State, postInput: (ChaosBagSimulatorContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            Hero(
                title = { Text("Chaos Bag Simulator") },
                subtitle = { Text("Tools") },
                size = BulmaSize.Small,
            )
            BulmaSection {
                Breadcrumbs(
                    NavigationRoute("ChaosBagSimulator", null, ArkhamApp.ChaosBagSimulator),
                    NavigationRoute("Tools", null, ArkhamApp.Tools),
                    NavigationRoute("Chaos Bag Simulator", null, ArkhamApp.ChaosBagSimulator),
                )
            }
            BulmaSection {
                Card {
                    if (state.scenario != null && state.scenario.chaosBag.isNotEmpty()) {
                        H4 { Text("Difficulty") }
                        Div({ classes("control") }) {
                            state.scenario.chaosBag.forEach { difficulty ->
                                Label(null, { classes("radio") }) {
                                    Input(type = InputType.Radio) {
                                        id("scenario-difficulty-${difficulty.difficulty.name}")
                                        onInput {
                                            postInput(
                                                ChaosBagSimulatorContract.Inputs.ChangeScenarioDifficulty(
                                                    difficulty.difficulty
                                                )
                                            )
                                        }
                                        name(difficulty.difficulty.name)
                                        checked(state.selectedDifficulty == difficulty.difficulty)
                                    }
                                    Text(" ${difficulty.difficulty.name} ")
                                }
                            }
                        }
                    }

                    H4 { Text("Chaos Bag") }
                    Div { Text(state.tokens.toString()) }

                    if (state.consumedTokens.isEmpty()) {
                        Button({
                            onClick { postInput(ChaosBagSimulatorContract.Inputs.DrawToken) }
                            classes("button")
                        }) { Text("Draw Token") }
                    } else {
                        Button({
                            onClick { postInput(ChaosBagSimulatorContract.Inputs.PutTokenBack(state.consumedTokens.last())) }
                            classes("button")
                        }) { Text("Put Back") }
                        Button({
                            onClick { postInput(ChaosBagSimulatorContract.Inputs.DrawToken) }
                            classes("button")
                        }) { Text("Draw Another Token") }
                        Button({
                            onClick { postInput(ChaosBagSimulatorContract.Inputs.PutAllTokensBack) }
                            classes("button")
                        }) { Text("Reset Bag") }
                    }
                    Hr { }

                    H4 { Text("Drawn Tokens") }
                    state.consumedTokens.forEach { token ->
                        Button({
                            onClick { postInput(ChaosBagSimulatorContract.Inputs.PutTokenBack(token)) }
                            classes("button")
                        }) { Text(token.toString()) }
                    }
                    Hr { }

                    H4 { Text("Remaining Tokens") }
                    Div { Text(state.remainingTokens.toString()) }
                    Hr { }
                }
            }
        }
    }
}
