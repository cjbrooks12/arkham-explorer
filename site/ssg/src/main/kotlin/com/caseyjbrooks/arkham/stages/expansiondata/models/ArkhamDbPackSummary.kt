package com.caseyjbrooks.arkham.stages.expansiondata.models

data class ArkhamDbPackSummary(
    val cyclePosition: Int,
    val name: String,
    val products: List<ArkhamDbProductSummary>,
)
