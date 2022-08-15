package com.caseyjbrooks.arkham.stages.api.utils

import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPack
import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion
import com.caseyjbrooks.arkham.utils.preprocessContent
import com.copperleaf.arkham.models.api.EncounterSet
import com.copperleaf.arkham.models.api.EncounterSetId

fun LocalArkhamHorrorExpansion.EncounterSet.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
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
        },
        products = allExpansionData
            .getProductsContainingEncounterSet(this.name)
            .map { it.asLiteOutput(expansionCode, packsApi) },
    )
}
