package com.copperleaf.arkham.models.api

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
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
    val productType: ProductType,
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
    val productType: ProductType,
)

@Serializable
data class ProductList(
    val products: List<ProductLite>,
)

@Serializable
enum class ProductType(val value: String) {
    @SerialName("Core Set")
    CoreSet("Core Set"),

    @SerialName("Deluxe Expansion")
    DeluxeExpansion("Deluxe Expansion"),

    @SerialName("Campaign Expansion")
    CampaignExpansion("Campaign Expansion"),

    @SerialName("Investigator Expansion")
    InvestigatorExpansion("Investigator Expansion"),

    @SerialName("Return To")
    ReturnTo("Return To"),

    @SerialName("Mythos Pack")
    MythosPack("Mythos Pack"),

    @SerialName("Scenario Pack")
    ScenarioPack("Scenario Pack"),

    @SerialName("Investigator Starter Deck")
    InvestigatorStarterDeck("Investigator Starter Deck"),
}
