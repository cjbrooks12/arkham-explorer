package com.copperleaf.arkham.models.api

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class ExpansionId(val id: String) : Comparable<ExpansionId> {
    override fun compareTo(other: ExpansionId): Int {
        return this.id.compareTo(other.id)
    }
}

@Serializable
data class Expansion(
    val name: String = "",
    val id: ExpansionId = ExpansionId(""),
    val code: String,
    val icon: String = "",
    val scenarios: List<Scenario> = emptyList(),
    val encounterSets: List<EncounterSet> = emptyList(),
    val investigators: List<Investigator> = emptyList(),
    val products: List<Product> = emptyList(),
)

@Serializable
data class ExpansionLite(
    val name: String = "",
    val id: ExpansionId = ExpansionId(""),
    val code: String,
    val icon: String = "",
    val scenarios: List<ScenarioId> = emptyList(),
    val encounterSets: List<EncounterSetId> = emptyList(),
    val investigators: List<InvestigatorId> = emptyList(),
    val products: List<ProductId> = emptyList(),
)

@Serializable
data class ExpansionList(
    val expansions: List<ExpansionLite> = emptyList()
)
