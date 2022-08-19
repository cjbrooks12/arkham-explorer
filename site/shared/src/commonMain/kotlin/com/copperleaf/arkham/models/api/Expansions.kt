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
    val id: ExpansionId,
    val name: String,
    val expansionCode: String,
    val icon: String,
    val expansionType: ExpansionType,
    val boxArt: String,
    val flavorText: String,
    val scenarios: List<ScenarioLite>,
    val encounterSets: List<EncounterSetLite>,
    val investigators: List<InvestigatorLite>,
    val products: List<ProductLite>,
    val campaignLogSchema: JsonElement,
    val campaignLogUiSchema: JsonElement,
)

@Serializable
data class ExpansionLite(
    val id: ExpansionId,
    val name: String,
    val expansionCode: String,
    val icon: String,
    val expansionType: ExpansionType,
    val boxArt: String,
    val flavorText: String,
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
