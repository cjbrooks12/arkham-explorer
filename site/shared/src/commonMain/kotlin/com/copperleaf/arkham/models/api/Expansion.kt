package com.copperleaf.arkham.models.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
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
    val expansionType: ExpansionType,
    val icon: String,
    val boxArt: String,
    val flavorText: String,
    val scenarios: List<Scenario>,
    val encounterSets: List<EncounterSet>,
    val investigators: List<Investigator>,
    val products: List<ProductLite>,
    val campaignLogSchema: JsonElement,
)

@Serializable
data class ExpansionLite(
    val name: String,
    val id: ExpansionId,
    val code: String,
    val expansionType: ExpansionType,
    val icon: String,
    val boxArt: String,
    val flavorText: String,
    val scenarios: List<ScenarioId>,
    val encounterSets: List<EncounterSetId>,
    val investigators: List<InvestigatorId>,
    val products: List<ProductLite>,
)

@Serializable
data class ExpansionList(
    val expansions: List<ExpansionLite>
)

@Serializable
sealed class ExpansionType {
    @Serializable
    @SerialName("cycle")
    object Cycle : ExpansionType()

    @Serializable
    @SerialName("return to")
    data class ReturnTo(val forCycle: String) : ExpansionType()

    @Serializable
    @SerialName("standalone")
    object Standalone : ExpansionType()
}
