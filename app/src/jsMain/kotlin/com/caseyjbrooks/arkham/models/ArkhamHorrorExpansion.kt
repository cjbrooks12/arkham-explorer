package com.caseyjbrooks.arkham.models

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.serialization.Serializable

@Serializable
data class ArkhamHorrorExpansion(
    val arkhamDbEntry: String = "",
    val encounterSets: List<EncounterSets> = listOf(),
    val icon: String = "",
    val id: String = "",
    val investigators: List<Investigators> = listOf(),
    val name: String = "",
    val releaseDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val returnTo: Boolean = false,
    val scenarios: List<Scenarios> = listOf()
) {
    @Serializable
    data class EncounterSets(
        val arkhamDbEntry: String = "",
        val icon: String = "",
        val name: String = ""
    )

    @Serializable
    data class Investigators(
        val arkhamDbEntry: String = "",
        val mainClass: MainClass = MainClass.Guardian,
        val name: String = ""
    ) {
        enum class MainClass(val value: String) {
            Guardian("Guardian"),

            Rogue("Rogue"),

            Seeker("Seeker"),

            Survivor("Survivor"),

            Mystic("Mystic"),

            Neutral("Neutral");
        }
    }

    @Serializable
    data class Scenarios(
        val arkhamDbEntry: String = "",
        val encounterSets: List<EncounterSets> = listOf(),
        val id: String = "",
        val name: String = "",
        val icon: String = ""
    ) {
        @Serializable
        data class EncounterSets(
            val conditional: Boolean = false,
            val name: String = "",
            val setAside: Boolean = false
        )
    }
}
