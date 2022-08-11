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
    val name: String,
    val id: ExpansionId,
    val code: String,
    val isReturnTo: Boolean,
    val hasReturnTo: Boolean,
    val returnToCode: String?,
    val icon: String,
    val boxArt: String,
    val flavorText: String,
    val scenarios: List<Scenario>,
    val encounterSets: List<EncounterSet>,
    val investigators: List<Investigator>,
    val products: List<Product>,
)

@Serializable
data class ExpansionLite(
    val name: String,
    val id: ExpansionId,
    val code: String,
    val isReturnTo: Boolean,
    val hasReturnTo: Boolean,
    val returnToCode: String?,
    val icon: String,
    val boxArt: String,
    val flavorText: String,
    val scenarios: List<ScenarioId>,
    val encounterSets: List<EncounterSetId>,
    val investigators: List<InvestigatorId>,
    val products: List<ProductId>,
)

@Serializable
data class ExpansionList(
    val expansions: List<ExpansionLite>
)
