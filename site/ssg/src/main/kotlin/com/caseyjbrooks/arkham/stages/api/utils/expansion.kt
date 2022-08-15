package com.caseyjbrooks.arkham.stages.api.utils

import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPack
import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion
import com.caseyjbrooks.arkham.utils.preprocessContent
import com.copperleaf.arkham.models.api.Expansion
import com.copperleaf.arkham.models.api.ExpansionId
import com.copperleaf.arkham.models.api.ExpansionLite
import com.copperleaf.arkham.models.api.ExpansionType

fun LocalArkhamHorrorExpansion.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
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
        scenarios = this.scenarios.map { it.asFullOutput(expansionCode, allExpansionData, packsApi) },
        encounterSets = this.encounterSets.map { it.asFullOutput(expansionCode, allExpansionData, packsApi) },
        investigators = this.investigators.map { it.asFullOutput(expansionCode, allExpansionData, packsApi) },
        products = this.products.map { it.asLiteOutput(expansionCode, packsApi) },
        campaignLogSchema = this.campaignLogSchema,
    )
}

fun LocalArkhamHorrorExpansion.asLiteOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
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
        scenarios = this.scenarios.map { it.asFullOutput(expansionCode, allExpansionData, packsApi).id },
        encounterSets = this.encounterSets.map { it.asFullOutput(expansionCode, allExpansionData, packsApi).id },
        investigators = this.investigators.map { it.asFullOutput(expansionCode, allExpansionData, packsApi).id },
        products = this.products.map { it.asLiteOutput(expansionCode, packsApi) },
    )
}
