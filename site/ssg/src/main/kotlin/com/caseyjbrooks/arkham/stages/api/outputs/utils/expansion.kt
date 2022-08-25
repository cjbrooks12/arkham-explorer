package com.caseyjbrooks.arkham.stages.api.outputs.utils

import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPack
import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion
import com.caseyjbrooks.arkham.utils.preprocessContent
import com.copperleaf.arkham.models.api.Expansion
import com.copperleaf.arkham.models.api.ExpansionId
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.ExpansionType
import com.copperleaf.arkham.models.api.ScenarioId

fun LocalArkhamHorrorExpansion.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
): Expansion {
    return Expansion(
        id = ExpansionId(this.id),
        name = this.name,
        expansionCode = expansionCode,
        icon = this.icon.preprocessContent(),
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
        boxArt = this.boxArt.preprocessContent(),
        flavorText = this.flavorText,
        scenarios = this.scenarios.map { it.asLiteOutput(expansionCode, allExpansionData, packsApi) },
        encounterSets = this.encounterSets.map { it.asLiteOutput(expansionCode, allExpansionData, packsApi) },
        investigators = this.investigators.map { it.asLiteOutput(expansionCode, allExpansionData, packsApi) },
        products = this.products.map { it.asLiteOutput(expansionCode, packsApi) },
        startScenario = this.startScenario.map { ScenarioId(allExpansionData.getScenarioByName(it).id) },
    )
}

@Suppress("UNUSED_PARAMETER")
fun LocalArkhamHorrorExpansion.asLiteOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
): ExpansionLite {
    return ExpansionLite(
        name = this.name,
        id = ExpansionId(this.id),
        expansionCode = expansionCode,
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
        startScenario = this.startScenario.map {
            allExpansionData.getScenarioByName(it).asLiteOutput(expansionCode, allExpansionData, packsApi)
        },
    )
}
