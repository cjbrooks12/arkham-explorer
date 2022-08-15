package com.caseyjbrooks.arkham.stages.api.utils

import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPack
import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion
import com.caseyjbrooks.arkham.utils.preprocessContent
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.Scenario
import com.copperleaf.arkham.models.api.ScenarioEncounterSet
import com.copperleaf.arkham.models.api.ScenarioId

fun LocalArkhamHorrorExpansion.Scenario.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
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
        products = allExpansionData
            .getProductsContainingScenario(this.name)
            .map { it.asLiteOutput(expansionCode, packsApi) },
    )
}

fun LocalArkhamHorrorExpansion.Scenario.ScenarioEncounterSet.asFullOutput(
    allExpansionData: List<LocalArkhamHorrorExpansion>
): ScenarioEncounterSet {
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
