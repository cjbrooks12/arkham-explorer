package com.caseyjbrooks.arkham.stages.api.inputs.models

import com.copperleaf.arkham.models.api.ExpansionType
import com.copperleaf.arkham.models.api.ScenarioChaosBag
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

@Serializable
data class LocalArkhamHorrorExpansion(
    val id: String = "",
    val code: String = "",
    val name: String = "",
    val products: List<Product> = emptyList(),
    val icon: String = "",
    val expansionType: ExpansionType = ExpansionType.Cycle,
    val boxArt: String = "",
    val flavorText: String = "",
    val scenarios: List<Scenario> = listOf(),
    val encounterSets: List<EncounterSet> = listOf(),
    val investigators: List<Investigator> = emptyList(),
    val startScenario: List<String> = emptyList(),
) {
    @Serializable
    data class Scenario(
        val id: String = "",
        val name: String = "",
        val icon: String = "",
        val encounterSets: List<ScenarioEncounterSet> = listOf(),
        val chaosBag: List<ScenarioChaosBag> = emptyList(),
        val nextScenario: List<String> = emptyList(),
        val campaignLogSchema: JsonElement = JsonNull,
        val campaignLogUiSchema: JsonElement = JsonNull,
    ) {
        @Serializable
        data class ScenarioEncounterSet(
            val name: String = "",
            val conditional: Boolean = false,
            val setAside: Boolean = false,
            val partial: Boolean = false,
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

    @Serializable
    data class Product(
        val id: String = "",
        val name: String? = null,
        val arkhamDbCode: String? = null,
        val releaseDate: LocalDate? = null,
        val officialProductUrl: String = "",
        val scenarios: List<String> = emptyList(),
        val encounterSets: List<String> = emptyList(),
        val investigators: List<String> = emptyList(),
    )
}
