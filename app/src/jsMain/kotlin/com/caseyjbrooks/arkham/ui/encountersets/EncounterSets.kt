package com.caseyjbrooks.arkham.ui.encountersets

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Text

@Composable
fun EncounterSetsScreen() {
    Text("Encounter Sets")

//    LazyColumn(
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        items(state.encounterSets) { (encounterSet, resolvedScenarios) ->
//            Card(elevation = 4.dp) {
//                Column(Modifier.padding(16.dp)) {
//                    Text(encounterSet.name, style = MaterialTheme.typography.h4)
//                    RemoteImage(encounterSet.icon, encounterSet.name, Modifier.size(48.dp))
//
//                    Column(Modifier.padding(16.dp)) {
//                        Text("Used in the following scenarios")
//                        resolvedScenarios.forEach { scenario ->
//                            Text(scenario.name)
//                            RemoteImage(scenario.icon, scenario.name, Modifier.size(48.dp))
//                        }
//                    }
//                }
//            }
//        }
//    }
}
