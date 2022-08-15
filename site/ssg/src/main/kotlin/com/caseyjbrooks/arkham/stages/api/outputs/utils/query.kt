package com.caseyjbrooks.arkham.stages.api.outputs.utils

import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion

fun List<LocalArkhamHorrorExpansion>.getScenarioByName(name: String): LocalArkhamHorrorExpansion.Scenario {
    return this
        .asSequence()
        .flatMap { it.scenarios }
        .filter { it.name == name }
        .singleOrNull()
        ?: error("Scenario with name '$name' not found")
}

fun List<LocalArkhamHorrorExpansion>.getEncounterSetByName(name: String): LocalArkhamHorrorExpansion.EncounterSet {
    return this
        .asSequence()
        .flatMap { it.encounterSets }
        .filter { it.name == name }
        .singleOrNull()
        ?: error("Encounter set with name '$name' not found")
}

fun List<LocalArkhamHorrorExpansion>.getInvestigatorByName(name: String): LocalArkhamHorrorExpansion.Investigator {
    return this
        .asSequence()
        .flatMap { it.investigators }
        .filter { it.name == name }
        .singleOrNull()
        ?: error("Investigator with name '$name' not found")
}

fun List<LocalArkhamHorrorExpansion>.getProductsContainingScenario(scenarioName: String): List<LocalArkhamHorrorExpansion.Product> {
    return this
        .asSequence()
        .flatMap { it.products }
        .filter { it.scenarios.contains(scenarioName) }
        .toList()
}

fun List<LocalArkhamHorrorExpansion>.getProductsContainingEncounterSet(encounterSetName: String): List<LocalArkhamHorrorExpansion.Product> {
    return this
        .asSequence()
        .flatMap { it.products }
        .filter { it.encounterSets.contains(encounterSetName) }
        .toList()
}

fun List<LocalArkhamHorrorExpansion>.getProductsContainingInvestigator(investigatorName: String): List<LocalArkhamHorrorExpansion.Product> {
    return this
        .asSequence()
        .flatMap { it.products }
        .filter { it.investigators.contains(investigatorName) }
        .toList()
}
