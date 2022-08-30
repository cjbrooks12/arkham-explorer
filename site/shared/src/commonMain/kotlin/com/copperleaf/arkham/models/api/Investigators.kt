package com.copperleaf.arkham.models.api

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class InvestigatorId(val id: String) : Comparable<InvestigatorId> {
    override fun compareTo(other: InvestigatorId): Int {
        return this.id.compareTo(other.id)
    }
}

@Serializable
data class InvestigatorDetails(
    val id: InvestigatorId,
    val name: String,
    val expansionCode: String,
    val portrait: String,
    val mainClass: InvestigatorClass,
    val health: Int,
    val sanity: Int,
    val products: List<ProductLite>,
)

@Serializable
data class InvestigatorLite(
    val id: InvestigatorId,
    val name: String,
    val expansionCode: String,
    val portrait: String,
    val mainClass: InvestigatorClass,
)

@Serializable
data class InvestigatorList(
    val investigators: List<InvestigatorLite>,
)

@Serializable
enum class InvestigatorClass {
    Guardian, Seeker, Rogue, Mystic, Survivor, Neutral
}
