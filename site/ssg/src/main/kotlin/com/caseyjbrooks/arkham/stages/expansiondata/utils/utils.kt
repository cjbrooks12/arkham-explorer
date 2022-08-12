package com.caseyjbrooks.arkham.stages.expansiondata.utils

import com.caseyjbrooks.arkham.stages.expansiondata.inputs.models.LocalArkhamHorrorExpansion
import com.caseyjbrooks.arkham.utils.preprocessContent
import com.copperleaf.arkham.models.api.EncounterSet
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.Expansion
import com.copperleaf.arkham.models.api.ExpansionId
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.ExpansionType
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

fun LocalArkhamHorrorExpansion.Scenario.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>
): Scenario {
    return Scenario(
        name = this.name,
        expansionCode = expansionCode,
        id = ScenarioId(this.id),
        icon = this.icon.preprocessContent(),
        encounterSets = this.encounterSets.map {
            it.asFullOutput(allExpansionData)
        },
        chaosBag = this.chaosBag,
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
        partial = this.partial,
    )
}

fun LocalArkhamHorrorExpansion.EncounterSet.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>
): EncounterSet {
    return EncounterSet(
        name = this.name,
        expansionCode = expansionCode,
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

fun LocalArkhamHorrorExpansion.Investigator.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>
): Investigator {
    return Investigator(
        name = this.name,
        expansionCode = expansionCode,
        id = InvestigatorId(this.id),
        portrait = "",
    )
}

fun LocalArkhamHorrorExpansion.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>
): Expansion {
    return Expansion(
        name = this.name,
        id = ExpansionId(this.id),
        code = expansionCode,
        expansionType = when (this.expansionType) {
            is ExpansionType.Cycle -> this.expansionType
            is ExpansionType.Standalone -> this.expansionType
            is ExpansionType.ReturnTo -> {
                val returnToCode = allExpansionData
                    .single { it.expansionType is ExpansionType.Cycle && it.name == this.expansionType.forCycle }
                    .code
                ExpansionType.ReturnTo(returnToCode)
            }
        },
        icon = this.icon.preprocessContent(),
        boxArt = this.boxArt.preprocessContent(),
        flavorText = this.flavorText,
        scenarios = this.scenarios.map { it.asFullOutput(expansionCode, allExpansionData) },
        encounterSets = this.encounterSets.map { it.asFullOutput(expansionCode, allExpansionData) },
        investigators = this.investigators.map { it.asFullOutput(expansionCode, allExpansionData) },
        products = emptyList(),
        campaignLogSchema = this.campaignLogSchema,
    )
}

fun LocalArkhamHorrorExpansion.asLiteOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>
): ExpansionLite {
    return ExpansionLite(
        name = this.name,
        id = ExpansionId(this.id),
        code = expansionCode,
        expansionType = when (this.expansionType) {
            is ExpansionType.Cycle -> this.expansionType
            is ExpansionType.Standalone -> this.expansionType
            is ExpansionType.ReturnTo -> {
                val returnToCode = allExpansionData
                    .single { it.expansionType is ExpansionType.Cycle && it.name == this.expansionType.forCycle }
                    .code
                ExpansionType.ReturnTo(returnToCode)
            }
        },
        icon = this.icon.preprocessContent(),
        boxArt = this.boxArt.preprocessContent(),
        flavorText = this.flavorText,
        scenarios = this.scenarios.map { it.asFullOutput(expansionCode, allExpansionData).id },
        encounterSets = this.encounterSets.map { it.asFullOutput(expansionCode, allExpansionData).id },
        investigators = this.investigators.map { it.asFullOutput(expansionCode, allExpansionData).id },
        products = emptyList(),
    )
}
