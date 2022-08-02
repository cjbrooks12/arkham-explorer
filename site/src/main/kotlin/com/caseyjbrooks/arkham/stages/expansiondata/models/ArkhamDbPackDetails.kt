package com.caseyjbrooks.arkham.stages.expansiondata.models

data class ArkhamDbPackDetails(
    val cyclePosition: Int,
    val name: String,
    val products: List<ArkhamDbProductDetails>,
)
