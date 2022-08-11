package com.copperleaf.arkham.models.api

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class ScenarioId(val id: String) : Comparable<ScenarioId> {
    override fun compareTo(other: ScenarioId): Int {
        return this.id.compareTo(other.id)
    }
}

@Serializable
data class Scenario(
    val name: String,
    val id: ScenarioId,
    val icon: String,
    val encounterSets: List<ScenarioEncounterSet>,
)

@Serializable
data class ScenarioList(
    val scenarios: List<Scenario>,
)
