package com.copperleaf.arkham.models

import kotlinx.serialization.Serializable

@Serializable
data class ArkhamHorrorExpansionsIndex(
    val expansions: List<ArkhamHorrorExpansionIndex>
) {
    @Serializable
    data class ArkhamHorrorExpansionIndex(
        val slug: String,
        val productCodes: List<String>,
    )
}
