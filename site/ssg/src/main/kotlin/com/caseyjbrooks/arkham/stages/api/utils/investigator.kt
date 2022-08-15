package com.caseyjbrooks.arkham.stages.api.utils

import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPack
import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion
import com.copperleaf.arkham.models.api.Investigator
import com.copperleaf.arkham.models.api.InvestigatorId

fun LocalArkhamHorrorExpansion.Investigator.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
): Investigator {
    return Investigator(
        name = this.name,
        expansionCode = expansionCode,
        id = InvestigatorId(this.id),
        portrait = "",
        products = allExpansionData
            .getProductsContainingInvestigator(this.name)
            .map { it.asLiteOutput(expansionCode, packsApi) },
    )
}
