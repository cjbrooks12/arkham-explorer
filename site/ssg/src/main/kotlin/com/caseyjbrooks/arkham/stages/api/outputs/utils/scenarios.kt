package com.caseyjbrooks.arkham.stages.api.outputs.utils

import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPack
import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion
import com.caseyjbrooks.arkham.utils.preprocessContent
import com.copperleaf.arkham.models.api.ScenarioDetails
import com.copperleaf.arkham.models.api.ScenarioEncounterSet
import com.copperleaf.arkham.models.api.ScenarioId
import com.copperleaf.arkham.models.api.ScenarioLite

fun LocalArkhamHorrorExpansion.Scenario.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
): ScenarioDetails {
    return ScenarioDetails(
        id = ScenarioId(this.id),
        name = this.name,
        expansionCode = expansionCode,
        icon = this.icon.preprocessContent(),
        encounterSets = this.encounterSets.map {
            it.asFullOutput(expansionCode, allExpansionData, packsApi)
        },
        chaosBag = this.chaosBag,
        products = allExpansionData
            .getProductsContainingScenario(this.name)
            .map { it.asLiteOutput(expansionCode, packsApi) },
    )
}

fun LocalArkhamHorrorExpansion.Scenario.ScenarioEncounterSet.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
): ScenarioEncounterSet {
    return ScenarioEncounterSet(
        encounterSet = allExpansionData
            .getEncounterSetByName(
                name = this.name
            )
            .asLiteOutput(expansionCode, allExpansionData, packsApi),
        conditional = this.conditional,
        setAside = this.setAside,
        partial = this.partial,
    )
}

@Suppress("UNUSED_PARAMETER")
fun LocalArkhamHorrorExpansion.Scenario.asLiteOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
): ScenarioLite {
    return ScenarioLite(
        id = ScenarioId(this.id),
        name = this.name,
        expansionCode = expansionCode,
        icon = this.icon.preprocessContent(),
    )
}
