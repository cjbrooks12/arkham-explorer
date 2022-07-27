package com.caseyjbrooks.arkham.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.encountersets.EncounterSetsScreen
import com.caseyjbrooks.arkham.ui.expansions.ExpansionsScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNotFound
import org.jetbrains.compose.web.dom.Text

@Composable
fun MainApplication(injector: ArkhamInjector) {
    val routerVm = remember { injector.routerViewModel() }
    val routerVmState by routerVm.observeStates().collectAsState()

    when(val destination = routerVmState.currentDestinationOrNotFound) {
        is Destination -> {
            when (destination.originalRoute) {
                ArkhamApp.Main -> {
                    Text("Main")
                }
                ArkhamApp.Expansions -> {
                    ExpansionsScreen()
                }
                ArkhamApp.ExpansionDetails -> {
                    Text("Expansion Details")
                    val expansionName = destination.pathParameters["expansionName"]!!
                    Text("expansionName: $expansionName")
                }
                ArkhamApp.EncounterSets -> {
                    EncounterSetsScreen()
                }
                ArkhamApp.EncounterSetDetails -> {
                    Text("Encounter Set Details")
                    val encounterSetName = destination.pathParameters["encounterSetName"]!!
                    Text("encounterSetName: $encounterSetName")
                }
            }
        }
        else -> {
            Text("Route not found")
        }
    }
}
