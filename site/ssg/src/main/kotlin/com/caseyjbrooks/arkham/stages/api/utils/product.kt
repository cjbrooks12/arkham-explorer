package com.caseyjbrooks.arkham.stages.api.utils

import com.caseyjbrooks.arkham.stages.api.inputs.models.ArkhamDbPack
import com.caseyjbrooks.arkham.stages.api.inputs.models.LocalArkhamHorrorExpansion
import com.copperleaf.arkham.models.api.Product
import com.copperleaf.arkham.models.api.ProductId
import com.copperleaf.arkham.models.api.ProductLite
import kotlinx.datetime.LocalDate

fun LocalArkhamHorrorExpansion.Product.asFullOutput(
    expansionCode: String,
    allExpansionData: List<LocalArkhamHorrorExpansion>,
    packsApi: List<ArkhamDbPack>,
): Product {
    val arkhamDbEntry = if (this.arkhamDbCode != null) {
        packsApi.single { it.code == this.arkhamDbCode }
    } else {
        null
    }

    return Product(
        name = this.name ?: arkhamDbEntry?.name ?: error("name (expansion=$expansionCode, product=${this.id})"),
        id = ProductId(this.id),
        expansionCode = expansionCode,
        releaseDate = this.releaseDate
            ?: arkhamDbEntry
                ?.available
                ?.takeIf { it.isNotBlank() }
                ?.let { LocalDate.parse(it) }
            ?: error("releaseDate (expansion=$expansionCode, product=${this.id})"),
        officialProductUrl = this.officialProductUrl,
        scenarios = this
            .scenarios
            .map { allExpansionData.getScenarioByName(it) }
            .map { it.asFullOutput(expansionCode, allExpansionData, packsApi) },
        encounterSets = this
            .encounterSets
            .map { allExpansionData.getEncounterSetByName(it) }
            .map { it.asFullOutput(expansionCode, allExpansionData, packsApi) },
        investigators = this
            .investigators
            .map { allExpansionData.getInvestigatorByName(it) }
            .map { it.asFullOutput(expansionCode, allExpansionData, packsApi) },
    )
}

fun LocalArkhamHorrorExpansion.Product.asLiteOutput(
    expansionCode: String,
    packsApi: List<ArkhamDbPack>,
): ProductLite {
    val arkhamDbEntry = if (this.arkhamDbCode != null) {
        packsApi.single { it.code == this.arkhamDbCode }
    } else {
        null
    }

    return ProductLite(
        name = this.name ?: arkhamDbEntry?.name ?: error("name (expansion=$expansionCode, product=${this.id})"),
        id = ProductId(this.id),
    )
}
