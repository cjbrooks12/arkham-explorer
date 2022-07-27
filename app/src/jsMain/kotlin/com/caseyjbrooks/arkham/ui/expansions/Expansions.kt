package com.caseyjbrooks.arkham.ui.expansions

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Text

@Composable
fun ExpansionsScreen() {
    Text("Expansions")

//    LazyColumn(
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        items(state.expansions) { expansion ->
//            Card(elevation = 4.dp) {
//                Column(Modifier.padding(16.dp)) {
//                    Text(expansion.name, style = MaterialTheme.typography.h4)
//                    Text(expansion.releaseDate.toString())
//                    RemoteImage(expansion.icon, expansion.name, Modifier.size(48.dp))
//
//                    Column(Modifier.padding(16.dp)) {
//                        Text("Scenarios")
//
//                        expansion.scenarios.forEach { scenario ->
//                            Text(scenario.name, style = MaterialTheme.typography.h5)
//                            Divider()
//                            scenario.encounterSets.forEach { encounterSet ->
//                                val resolvedEncounterSet = remember(state.expansions, encounterSet) {
//                                    state.expansions
//                                        .asSequence()
//                                        .flatMap { it.encounterSets }
//                                        .firstOrNull { it.name == encounterSet.name }
//                                }
//
//                                if (resolvedEncounterSet == null) {
//                                    Text("could not find encounter set named '${encounterSet.name}'")
//                                } else {
//                                    Text(resolvedEncounterSet.name)
//                                    RemoteImage(resolvedEncounterSet.icon, resolvedEncounterSet.name, Modifier.size(48.dp))
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
}
