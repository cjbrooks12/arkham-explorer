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
data class EncounterSet(
    val name: String,
    val expansionCode: String,
    val id: EncounterSetId,
    val icon: String,
    val replaces: EncounterSetId?,
)

@Serializable
data class EncounterSetList(
    val encounterSets: List<EncounterSet>,
)

@Serializable
data class ScenarioEncounterSet(
    val name: String,
    val id: EncounterSetId,
    val icon: String,
    val conditional: Boolean,
    val setAside: Boolean,
    val partial: Boolean,
)
