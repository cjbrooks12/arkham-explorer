package com.copperleaf.arkham.models.api

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
    val name: String,
    val id: ProductId,
    val photos: List<String>,
)

@Serializable
data class ProductList(
    val products: List<Product>,
)
