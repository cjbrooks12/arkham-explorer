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
    val name: String = "",
    val id: EncounterSetId = EncounterSetId(""),
    val icon: String = "",
    val replaces: EncounterSetId? = null,
)

@Serializable
data class EncounterSetList(
    val encounterSets: List<EncounterSet> = emptyList()
)

@Serializable
data class ScenarioEncounterSet(
    val name: String = "",
    val id: EncounterSetId = EncounterSetId(""),
    val icon: String = "",
    val conditional: Boolean = false,
    val setAside: Boolean = false,
)
