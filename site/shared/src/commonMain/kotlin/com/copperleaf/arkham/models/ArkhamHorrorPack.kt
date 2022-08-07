package com.copperleaf.arkham.models

import kotlinx.serialization.Serializable

@Serializable
data class ArkhamHorrorExpansion(
    val name: String = "",
    val productCodes: List<String> = emptyList(),
    val icon: String = "",
    val scenarios: List<Scenario> = listOf(),
    val encounterSets: List<EncounterSet> = listOf(),
) {
    @Serializable
    data class Scenario(
        val name: String = "",
        val icon: String = "",
        val encounterSets: List<ScenarioEncounterSet> = listOf(),
    ) {
        @Serializable
        data class ScenarioEncounterSet(
            val name: String = "",
            val conditional: Boolean = false,
            val setAside: Boolean = false,
        )
    }

    @Serializable
    data class EncounterSet(
        val name: String = "",
        val icon: String = "",
    )
}
