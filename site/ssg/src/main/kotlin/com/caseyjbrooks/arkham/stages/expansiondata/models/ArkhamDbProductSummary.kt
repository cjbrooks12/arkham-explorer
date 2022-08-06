package com.caseyjbrooks.arkham.stages.expansiondata.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArkhamDbProductSummary(
    val available: String = "",
    val code: String = "",
    @SerialName("cycle_position")
    val cyclePosition: Int = 0,
    val id: Int = 0,
    val known: Int = 0,
    val name: String = "",
    val position: Int = 0,
    val total: Int = 0,
    val url: String = ""
)
