package com.caseyjbrooks.arkham.stages.expansiondata.inputs.models

import kotlinx.serialization.Serializable

@Serializable
data class LocalArkhamHorrorExpansion(
    val id: String = "",
    val name: String = "",
    val productCodes: List<String> = emptyList(),
    val icon: String = "",
    val scenarios: List<Scenario> = listOf(),
    val encounterSets: List<EncounterSet> = listOf(),
    val investigators: List<Investigator> = emptyList(),
) {
    @Serializable
    data class Scenario(
        val id: String = "",
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
        val id: String = "",
        val name: String = "",
        val icon: String = "",
        val replaces: String = "",
    )

    @Serializable
    data class Investigator(
        val id: String = "",
        val name: String = "",
        val mainClass: String = "",
    )
}
