package com.copperleaf.arkham.models.api

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class ProductId(val id: String) : Comparable<ProductId> {
    override fun compareTo(other: ProductId): Int {
        return this.id.compareTo(other.id)
    }
}

@Serializable
data class ProductDetails(
    val id: ProductId,
    val name: String,
    val expansionCode: String,
    val releaseDate: LocalDate,
    val officialProductUrl: String,
    val scenarios: List<ScenarioLite>,
    val encounterSets: List<EncounterSetLite>,
    val investigators: List<InvestigatorLite>,
)

@Serializable
data class ProductLite(
    val id: ProductId,
    val name: String,
    val expansionCode: String,
)

@Serializable
data class ProductList(
    val products: List<ProductDetails>,
)
