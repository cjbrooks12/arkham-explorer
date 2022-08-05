package com.caseyjbrooks.arkham.stages.expansiondata.models

import kotlinx.serialization.Serializable

@Serializable
class ArkhamDbProductDetails(
    val summary: ArkhamDbProductSummary,
    val cards: List<ArkhamDbCard>,
)
