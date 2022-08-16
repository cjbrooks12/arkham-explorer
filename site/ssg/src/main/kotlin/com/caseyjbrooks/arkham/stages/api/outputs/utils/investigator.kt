package com.caseyjbrooks.arkham.stages.api.outputs.utils

import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPack
import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPackCards
import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion
import com.copperleaf.arkham.models.api.InvestigatorDetails
import com.copperleaf.arkham.models.api.InvestigatorId
import com.copperleaf.arkham.models.api.InvestigatorLite

fun LocalArkhamHorrorExpansion.Investigator.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
    packCards: List<ArkhamDbPackCards>,
): InvestigatorDetails {
    val matchingInvestigator = packCards
        .asSequence()
        .flatMap { it.cards }
        .firstOrNull { it.name == this@asFullOutput.name }

    return InvestigatorDetails(
        id = InvestigatorId(this.id),
        name = this.name,
        expansionCode = expansionCode,
        portrait = "",
        products = allExpansionData
            .getProductsContainingInvestigator(this.name)
            .map { it.asLiteOutput(expansionCode, packsApi) },
        health = matchingInvestigator?.health ?: 0,
        sanity = matchingInvestigator?.sanity ?: 0,
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
