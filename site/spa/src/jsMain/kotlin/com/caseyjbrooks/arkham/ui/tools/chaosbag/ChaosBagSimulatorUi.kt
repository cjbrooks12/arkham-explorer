package com.caseyjbrooks.arkham.ui.tools.chaosbag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.DynamicGrid
import com.caseyjbrooks.arkham.utils.GridItem
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
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Tbody
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Thead
import org.jetbrains.compose.web.dom.Tr
import kotlin.math.roundToInt

@Suppress("UNUSED_PARAMETER")
object ChaosBagSimulatorUi {
    @Composable
    fun Page(injector: ArkhamInjector, scenarioId: ScenarioId?) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, scenarioId) {
            injector.chaosBagSimulatorViewModel(
                coroutineScope,
                scenarioId
            )
        }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: ChaosBagSimulatorContract.State, postInput: (ChaosBagSimulatorContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            Header()
            Body(state, postInput)
        }
    }

    @Composable
    fun Header() {
        Hero(
            title = { Text("Chaos Bag Simulator") },
            subtitle = { Text("Tools") },
            size = BulmaSize.Small,
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ArkhamApp.Home),
                NavigationRoute("Tools", null, ArkhamApp.Tools),
                NavigationRoute("Chaos Bag Simulator", null, ArkhamApp.ChaosBagSimulator),
            )
        }
    }

    @Composable
    fun Body(state: ChaosBagSimulatorContract.State, postInput: (ChaosBagSimulatorContract.Inputs) -> Unit) {
        DynamicGrid(
            GridItem {
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
                    Div { Text(state.allTokens.toString()) }
                }
            },
            GridItem {
                Card {
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
            },
            GridItem {
                Card(
                    title = "Table of Terrible",
                    description = "This table gives a rough idea of how likely you are to pass a skill test. It does " +
                        "not account for characters' Elder Sign abilities or tokens/abilities that make you draw again."
                ) {
                    state.referenceCard?.let { referenceCard ->
                        H4 { Text("Reference Card") }
                        Table({ classes("table") }) {
                            Thead {
                                Tr {
                                    Th { Text("Special Token") }
                                    Th { Text("Current Value") }
                                    Th { Text("Token Text") }
                                }
                            }
                            Tbody {
                                referenceCard.tokens.forEach {
                                    Tr {
                                        Th { Text("${it.token}") }
                                        Th {
                                            val currentVariableValue = it.token.modifierValue(
                                                referenceCard,
                                                state.chaosBagVariableModifierValues
                                            ) ?: 0
                                            Text("$currentVariableValue")
                                        }
                                        Th { Text(it.text) }
                                    }
                                }
                            }
                        }

                        Hr { }
                    }

                    if (state.chaosTokenVariableKeys.isNotEmpty()) {
                        H4 { Text("Reference Card Variables") }
                        state.chaosTokenVariableKeys.forEach { key ->
                            val currentVariableValue = state.chaosBagVariableModifierValues[key] ?: 0
                            Div({ classes("field", "has-addons") }) {
                                Label(null, { classes("label") }) { Text(key) }
                            }
                            Div({ classes("field", "has-addons") }) {
                                Div({ classes("control") }) {
                                    A(null, {
                                        classes("button", "is-info")
                                        onClick {
                                            postInput(
                                                ChaosBagSimulatorContract.Inputs.UpdateTokenModifierValue(
                                                    key,
                                                    currentVariableValue - 1
                                                )
                                            )
                                        }
                                    }) {
                                        Text("-")
                                    }
                                }
                                Div({ classes("control") }) {
                                    Input(InputType.Text) {
                                        classes("input")
                                        value(currentVariableValue)
                                        onInput {
                                            postInput(
                                                ChaosBagSimulatorContract.Inputs.UpdateTokenModifierValue(
                                                    key,
                                                    it.value.toIntOrNull() ?: currentVariableValue
                                                )
                                            )
                                        }
                                    }
                                }
                                Div({ classes("control") }) {
                                    A(null, {
                                        classes("button", "is-info")
                                        onClick {
                                            postInput(
                                                ChaosBagSimulatorContract.Inputs.UpdateTokenModifierValue(
                                                    key,
                                                    currentVariableValue + 1
                                                )
                                            )
                                        }
                                    }) {
                                        Text("+")
                                    }
                                }
                            }
                        }
                        Hr { }
                    }

                    H4 { Text("Probabilities") }
                    Table({ classes("table") }) {
                        Thead {
                            Tr {
                                Th { Text("Advantage") }
                                Th { Text("Number of tokens that could fail") }
                                Th { Text("Chance of Success") }
                            }
                        }
                        Tbody {
                            state.tableOfTerrible.forEach {
                                Tr {
                                    Th { Text("${it.advantage}") }
                                    Th { Text("${it.numberOfTokensThatFail}") }
                                    Th { Text("${(it.chanceOfSuccess * 100).roundToInt()}%") }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
