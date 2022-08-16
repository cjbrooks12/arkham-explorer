package com.caseyjbrooks.arkham.ui.tools.investigatortracker

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
import com.caseyjbrooks.arkham.utils.theme.bulma.ButtonGroup
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.arkham.models.api.InvestigatorId
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text

object InvestigatorTrackerUi {
    @Composable
    fun Page(injector: ArkhamInjector, investigatorId: InvestigatorId?) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, investigatorId) {
            injector.investigatorTrackerViewModel(
                coroutineScope,
                investigatorId
            )
        }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: InvestigatorTrackerContract.State, postInput: (InvestigatorTrackerContract.Inputs) -> Unit) {
        MainLayout(state.layout) { layoutState ->
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
                NavigationRoute("Investigator Tracker", null, ArkhamApp.InvestigatorTracker),
            )
        }
    }

    @Composable
    fun Body(state: InvestigatorTrackerContract.State, postInput: (InvestigatorTrackerContract.Inputs) -> Unit) {
        DynamicGrid(
            GridItem {
                Card {
                    if (state.investigator != null) {
                        H4 { Text(state.investigator.name) }

                        ButtonGroup {
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateDamage(1)) }) { Text("-") }
                            ButtonInGroup({  }) { Text("Health: ${state.healthRemaining} / ${state.health}") }
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateDamage(-1)) }) { Text("+") }
                        }
                        ButtonGroup {
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateHorror(1)) }) { Text("-") }
                            ButtonInGroup({  }) { Text("Sanity: ${state.sanityRemaining} / ${state.sanity}") }
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateHorror(-1)) }) { Text("+") }
                        }
                        ButtonGroup {
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateResources(-1)) }) { Text("-") }
                            ButtonInGroup({  }) { Text("Resources: ${state.resources}") }
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateResources(1)) }) { Text("+") }
                        }
                        ButtonGroup {
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateClues(-1)) }) { Text("-") }
                            ButtonInGroup({  }) { Text("Clues: ${state.clues}") }
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateClues(1)) }) { Text("+") }
                        }
                    }
                    else {
                        H4 { Text("Anonymous Investigator") }

                        ButtonGroup {
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateHealth(-1)) }) { Text("--") }
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateDamage(1)) }) { Text("-") }
                            ButtonInGroup({  }) { Text("Health: ${state.healthRemaining} / ${state.health}") }
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateDamage(-1)) }) { Text("+") }
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateHealth(1)) }) { Text("++") }
                        }
                        ButtonGroup {
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateSanity(-1)) }) { Text("--") }
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateHorror(1)) }) { Text("-") }
                            ButtonInGroup({  }) { Text("Sanity: ${state.sanityRemaining} / ${state.sanity}") }
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateHorror(-1)) }) { Text("+") }
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateSanity(1)) }) { Text("++") }
                        }
                        ButtonGroup {
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateResources(-1)) }) { Text("-") }
                            ButtonInGroup({  }) { Text("Resources: ${state.resources}") }
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateResources(1)) }) { Text("+") }
                        }
                        ButtonGroup {
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateClues(-1)) }) { Text("-") }
                            ButtonInGroup({  }) { Text("Clues: ${state.clues}") }
                            ButtonInGroup({ postInput(InvestigatorTrackerContract.Inputs.UpdateClues(1)) }) { Text("+") }
                        }
                    }
                }
            },
        )
    }
}
