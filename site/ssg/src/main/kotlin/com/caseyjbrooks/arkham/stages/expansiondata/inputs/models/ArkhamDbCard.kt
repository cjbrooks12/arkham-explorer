package com.caseyjbrooks.arkham.stages.expansiondata.inputs.models

import kotlinx.serialization.Serializable

@Serializable
data class ArkhamDbCard(
    val pack_code: String? = null,
    val pack_name: String? = null,
    val type_code: String? = null,
    val type_name: String? = null,
    val subtype_code: String? = null,
    val subtype_name: String? = null,
    val faction_code: String? = null,
    val faction_name: String? = null,
    val faction2_code: String? = null,
    val faction2_name: String? = null,
    val faction3_code: String? = null,
    val faction3_name: String? = null,
    val encounter_code: String? = null,
    val encounter_name: String? = null,
    val encounter_position: Int? = null,
    val position: Int? = null,
    val exceptional: Boolean? = null,
    val myriad: Boolean? = null,
    val code: String? = null,
    val name: String? = null,
    val real_name: String? = null,
    val subname: String? = null,
    val cost: Int? = null,
    val text: String? = null,
    val real_text: String? = null,
    val errata_date: ErrataDate? = null,
    val quantity: Int? = null,
    val skill_willpower: Int? = null,
    val skill_intellect: Int? = null,
    val skill_combat: Int? = null,
    val skill_agility: Int? = null,
    val skill_wild: Int? = null,
    val xp: Int? = null,
    val health: Int? = null,
    val health_per_investigator: Boolean? = null,
    val sanity: Int? = null,
    val enemy_damage: Int? = null,
    val enemy_horror: Int? = null,
    val enemy_fight: Int? = null,
    val enemy_evade: Int? = null,
    val deck_limit: Int? = null,
    val slot: String? = null,
    val real_slot: String? = null,
    val traits: String? = null,
    val real_traits: String? = null,
    val restrictions: Restrictions? = null,
    val deck_requirements: DeckRequirements? = null,
    @Serializable(with = ArkhamDbDeckOptionsSerializer::class)
    val deck_options: List<DeckOptions>? = null,
    val flavor: String? = null,
    val illustrator: String? = null,
    val is_unique: Boolean? = null,
    val permanent: Boolean? = null,
    val double_sided: Boolean? = null,
    val back_name: String? = null,
    val back_text: String? = null,
    val back_flavor: String? = null,
    val backimagesrc: String? = null,
    val octgn_id: String? = null,
    val url: String? = null,
    val imagesrc: String? = null,
    val spoiler: Int? = null,
    val doom: Int? = null,
    val clues: Int? = null,
    val stage: Int? = null,
    val shroud: Int? = null,
    val victory: Int? = null,
    val duplicated_by: List<String>? = null,
    val alternated_by: List<String>? = null,

    // only used in linked cards
    val id: Int? = null,
    val linked_to_code: String? = null,
    val linked_to_name: String? = null,
    val linked_card: ArkhamDbCard? = null,
    val bonded_to: String? = null,
    val bonded_count: Int? = null,
    val bonded_cards: List<ArkhamDbCard>? = null,
    val clues_fixed: Boolean? = null,
    val vengeance: Int? = null,
    val exile: Boolean? = null,
    val hidden: Boolean? = null,

    // duplicated cards
    val duplicate_of_code: String? = null,
    val duplicate_of_name: String? = null,
    val alternate_of_code: String? = null,
    val alternate_of_name: String? = null,
) {
    @Serializable
    data class ErrataDate(
        val date: String? = null,
        val timezone_type: Int? = null,
        val timezone: String? = null,
    )

    @Serializable
    data class DeckRequirements(
        val size: Int? = null,
        val card: Map<String, Map<String, String>>? = null,
        val random: List<DeckRequirementsRandom>? = null,
    ) {
        @Serializable
        data class DeckRequirementsRandom(
            val target: String? = null,
            val value: String? = null,
        )
    }

    @Serializable
    data class DeckOptions(
        val faction: List<String>? = null,
        val level: DeckOptionsLevel? = null,
        val limit: Int? = null,
        val error: String? = null,
        val not: Boolean? = null,
        val trait: List<String>? = null,
        val uses: List<String>? = null,
        val atleast: AtLeast? = null,
        val text: List<String>? = null,
        val name: String? = null,
        val deck_size_select: List<String>? = null,
        val faction_select: List<String>? = null,
        val type: List<String>? = null,
        val id: String? = null,
        val slot: List<String>? = null,
        val option_select: List<DeckOptions>? = null,
        val size: Int? = null,
    ) {
        @Serializable
        data class DeckOptionsLevel(
            val min: Int? = null,
            val max: Int? = null,
        )

        @Serializable
        data class AtLeast(
            val factions: Int? = null,
            val min: Int? = null,
        )
    }

    @Serializable
    data class Restrictions(
        val investigator: Map<String, String>? = null,
    )
}
