package com.copperleaf.arkham.models.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class ScenarioId(val id: String) : Comparable<ScenarioId> {
    override fun compareTo(other: ScenarioId): Int {
        return this.id.compareTo(other.id)
    }
}

@Serializable
data class ScenarioDetails(
    val id: ScenarioId,
    val name: String,
    val expansionCode: String,
    val icon: String,
    val encounterSets: List<ScenarioEncounterSet>,
    val chaosBag: List<ScenarioChaosBag>,
    val referenceCard: List<ScenarioReferenceCard>,
    val products: List<ProductLite>,
    val nextScenario: List<ScenarioId>,
    val campaignLogSchema: JsonElement,
    val campaignLogUiSchema: JsonElement,
)

@Serializable
data class ScenarioLite(
    val id: ScenarioId,
    val name: String,
    val expansionCode: String,
    val icon: String,
)

@Serializable
data class ScenarioList(
    val scenarios: List<ScenarioLite>,
)
