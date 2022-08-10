package com.caseyjbrooks.arkham.stages.expansiondata.utils

import com.caseyjbrooks.arkham.stages.expansiondata.inputs.models.LocalArkhamHorrorExpansion
import com.caseyjbrooks.arkham.utils.preprocessContent
import com.copperleaf.arkham.models.api.EncounterSet
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.Expansion
import com.copperleaf.arkham.models.api.ExpansionId
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.Investigator
import com.copperleaf.arkham.models.api.InvestigatorId
import com.copperleaf.arkham.models.api.Scenario
import com.copperleaf.arkham.models.api.ScenarioEncounterSet
import com.copperleaf.arkham.models.api.ScenarioId

fun List<LocalArkhamHorrorExpansion>.getEncounterSetByName(name: String): LocalArkhamHorrorExpansion.EncounterSet {
    return this
        .asSequence()
        .flatMap { it.encounterSets }
        .filter { it.name == name }
        .singleOrNull()
        ?: error("Encounter set with name '$name' not found")
}

fun LocalArkhamHorrorExpansion.Scenario.asFullOutput(allExpansionData: List<LocalArkhamHorrorExpansion>): Scenario {
    return Scenario(
        name = this.name,
        id = ScenarioId(this.id),
        icon = this.icon.preprocessContent(),
        encounterSets = this.encounterSets.map {
            it.asFullOutput(allExpansionData)
        }
    )
}

fun LocalArkhamHorrorExpansion.Scenario.ScenarioEncounterSet.asFullOutput(allExpansionData: List<LocalArkhamHorrorExpansion>): ScenarioEncounterSet {
    val matchingEncounterSet = allExpansionData
        .getEncounterSetByName(
            name = this.name
        )
    return ScenarioEncounterSet(
        name = this.name,
        id = EncounterSetId(matchingEncounterSet.id),
        icon = matchingEncounterSet.icon.preprocessContent(),
        conditional = this.conditional,
        setAside = this.setAside,
    )
}

fun LocalArkhamHorrorExpansion.EncounterSet.asFullOutput(allExpansionData: List<LocalArkhamHorrorExpansion>): EncounterSet {
    return EncounterSet(
        name = this.name,
        id = EncounterSetId(this.id),
        icon = this.icon.preprocessContent(),
        replaces = this.replaces.takeIf { it.isNotBlank() }?.let {
            val matchingEncounterSet = allExpansionData
                .getEncounterSetByName(
                    name = this.replaces
                )

            EncounterSetId(matchingEncounterSet.id)
        }
    )
}

fun LocalArkhamHorrorExpansion.Investigator.asFullOutput(allExpansionData: List<LocalArkhamHorrorExpansion>): Investigator {
    return Investigator(
        name = this.name,
        id = InvestigatorId(this.id),
        portrait = "",
    )
}

fun LocalArkhamHorrorExpansion.asFullOutput(expansionSlug: String, allExpansionData: List<LocalArkhamHorrorExpansion>): Expansion {
    return Expansion(
        name = this.name,
        id = ExpansionId(this.id),
        code = expansionSlug,
        icon = this.icon.preprocessContent(),
        scenarios = this.scenarios.map { it.asFullOutput(allExpansionData) },
        encounterSets = this.encounterSets.map { it.asFullOutput(allExpansionData) },
        investigators = this.investigators.map { it.asFullOutput(allExpansionData) },
        products = emptyList(),
    )
}

fun LocalArkhamHorrorExpansion.asLiteOutput(expansionSlug: String, allExpansionData: List<LocalArkhamHorrorExpansion>): ExpansionLite {
    return ExpansionLite(
        name = this.name,
        id = ExpansionId(this.id),
        code = expansionSlug,
        icon = this.icon.preprocessContent(),
        scenarios = this.scenarios.map { it.asFullOutput(allExpansionData).id },
        encounterSets = this.encounterSets.map { it.asFullOutput(allExpansionData).id },
        investigators = this.investigators.map { it.asFullOutput(allExpansionData).id },
        products = emptyList(),
    )
}
