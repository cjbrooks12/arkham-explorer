package com.caseyjbrooks.arkham.models

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.serialization.Serializable

@Serializable
data class ArkhamHorrorExpansion(
    val arkhamDbEntry: String = "",
    val encounterSets: List<EncounterSet> = listOf(),
    val icon: String = "",
    val id: String = "",
    val investigators: List<Investigator> = listOf(),
    val name: String = "",
    val releaseDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val returnTo: Boolean = false,
    val scenarios: List<Scenario> = listOf()
) {
    @Serializable
    data class EncounterSet(
        val arkhamDbEntry: String = "",
        val icon: String = "",
        val name: String = ""
    )

    @Serializable
    data class Investigator(
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
    data class Scenario(
        val arkhamDbEntry: String = "",
        val encounterSets: List<ScenarioEncounterSet> = listOf(),
        val id: String = "",
        val name: String = "",
        val icon: String = ""
    ) {
        @Serializable
        data class ScenarioEncounterSet(
            val conditional: Boolean = false,
            val name: String = "",
            val setAside: Boolean = false
        )
    }
}
