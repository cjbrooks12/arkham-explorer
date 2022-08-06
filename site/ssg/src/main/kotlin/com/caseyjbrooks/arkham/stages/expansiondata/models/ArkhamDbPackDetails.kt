package com.caseyjbrooks.arkham.stages.expansiondata.models

import kotlinx.serialization.Serializable

@Serializable
data class ArkhamDbPackDetails(
    val cyclePosition: Int,
    val name: String,
    val products: List<ArkhamDbProductDetails>,
)
