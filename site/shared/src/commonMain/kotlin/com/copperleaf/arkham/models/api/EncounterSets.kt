package com.copperleaf.arkham.models.api

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class EncounterSetId(val id: String) : Comparable<EncounterSetId> {
    override fun compareTo(other: EncounterSetId): Int {
        return this.id.compareTo(other.id)
    }
}

@Serializable
data class EncounterSetDetails(
    val id: EncounterSetId,
    val name: String,
    val expansionCode: String,
    val icon: String,
    val replaces: EncounterSetId?,
    val products: List<ProductLite>,
)

@Serializable
data class EncounterSetLite(
    val id: EncounterSetId,
    val name: String,
    val expansionCode: String,
    val icon: String,
)

@Serializable
data class EncounterSetList(
    val encounterSets: List<EncounterSetDetails>,
)

@Serializable
data class ScenarioEncounterSet(
    val encounterSet: EncounterSetLite,
    val conditional: Boolean,
    val setAside: Boolean,
    val partial: Boolean,
)
