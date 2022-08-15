package com.caseyjbrooks.arkham.stages.api.outputs.utils

import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPack
import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion
import com.caseyjbrooks.arkham.utils.preprocessContent
import com.copperleaf.arkham.models.api.EncounterSetDetails
import com.copperleaf.arkham.models.api.EncounterSetId
import com.copperleaf.arkham.models.api.EncounterSetLite

fun LocalArkhamHorrorExpansion.EncounterSet.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
): EncounterSetDetails {
    return EncounterSetDetails(
        id = EncounterSetId(this.id),
        name = this.name,
        expansionCode = expansionCode,
        icon = this.icon.preprocessContent(),
        replaces = this.replaces.takeIf { it.isNotBlank() }?.let {
            val matchingEncounterSet = allExpansionData
                .getEncounterSetByName(
                    name = this.replaces
                )

            EncounterSetId(matchingEncounterSet.id)
        },
        products = allExpansionData
            .getProductsContainingEncounterSet(this.name)
            .map { it.asLiteOutput(expansionCode, packsApi) },
    )
}


fun LocalArkhamHorrorExpansion.EncounterSet.asLiteOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
): EncounterSetLite {
    return EncounterSetLite(
        id = EncounterSetId(this.id),
        name = this.name,
        expansionCode = expansionCode,
        icon = this.icon.preprocessContent(),
    )
}
