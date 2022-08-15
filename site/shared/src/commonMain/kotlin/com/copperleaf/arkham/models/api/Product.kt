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
data class Product(
    val id: ProductId,
    val name: String,
    val expansionCode: String,
    val releaseDate: LocalDate,
    val officialProductUrl: String,
    val scenarios: List<Scenario>,
    val encounterSets: List<EncounterSet>,
    val investigators: List<Investigator>,
)

@Serializable
data class ProductLite(
    val id: ProductId,
    val name: String,
)

@Serializable
data class ProductList(
    val products: List<Product>,
)
