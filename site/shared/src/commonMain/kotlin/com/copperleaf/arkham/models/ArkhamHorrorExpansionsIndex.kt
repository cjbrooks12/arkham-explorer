package com.copperleaf.arkham.models

import kotlinx.serialization.Serializable

@Serializable
data class ArkhamHorrorExpansionsIndex(
    val expansions: List<String>
)
