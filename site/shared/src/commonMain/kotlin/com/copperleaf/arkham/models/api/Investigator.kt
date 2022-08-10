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
data class Investigator(
    val name: String = "",
    val id: InvestigatorId = InvestigatorId(""),
    val portrait: String = "",
)

@Serializable
data class InvestigatorList(
    val investigators: List<Investigator> = emptyList()
)
