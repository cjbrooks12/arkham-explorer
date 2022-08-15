package com.caseyjbrooks.arkham.stages.api.outputs.utils

import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPack
import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion
import com.copperleaf.arkham.models.api.InvestigatorDetails
import com.copperleaf.arkham.models.api.InvestigatorId
import com.copperleaf.arkham.models.api.InvestigatorLite

fun LocalArkhamHorrorExpansion.Investigator.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
): InvestigatorDetails {
    return InvestigatorDetails(
        id = InvestigatorId(this.id),
        name = this.name,
        expansionCode = expansionCode,
        portrait = "",
        products = allExpansionData
            .getProductsContainingInvestigator(this.name)
            .map { it.asLiteOutput(expansionCode, packsApi) },
    )
}

fun LocalArkhamHorrorExpansion.Investigator.asLiteOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
): InvestigatorLite {
    return InvestigatorLite(
        id = InvestigatorId(this.id),
        name = this.name,
        expansionCode = expansionCode,
        portrait = "",
    )
}
