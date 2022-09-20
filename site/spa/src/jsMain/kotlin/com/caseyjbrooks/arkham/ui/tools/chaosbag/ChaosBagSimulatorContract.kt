package com.caseyjbrooks.arkham.ui.tools.chaosbag

import com.caseyjbrooks.arkham.utils.removeAllPreservingDuplicates
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ChaosToken
import com.copperleaf.arkham.models.api.ChaosTokenModifierValue
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ScenarioDetails
import com.copperleaf.arkham.models.api.ScenarioDifficulty
import com.copperleaf.arkham.models.api.ScenarioId
import com.copperleaf.arkham.models.api.ScenarioReferenceCard
import com.copperleaf.arkham.random.ImmutableRandom
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.datetime.Clock

object ChaosBagSimulatorContract {
    data class ChanceOfSuccess(
        val advantage: Int,
        val numberOfTokensThatFail: Int,
        val chanceOfSuccess: Double,
    )

    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val random: ImmutableRandom = ImmutableRandom(Clock.System.now().toEpochMilliseconds()),

        val scenarioId: ScenarioId? = null,
        val scenario: ScenarioDetails? = null,
        val selectedDifficulty: ScenarioDifficulty = ScenarioDifficulty.Standard,
        val referenceCards: List<ScenarioReferenceCard> = emptyList(),
        val referenceCard: ScenarioReferenceCard? = null,

        val allTokens: List<ChaosToken> = emptyList(),
        val consumedTokens: List<ChaosToken> = emptyList(),
        val chaosBagVariableModifierValues: Map<String, Int> = emptyMap(),
    ) {
        val remainingTokens: List<ChaosToken> = allTokens.removeAllPreservingDuplicates(consumedTokens)
        val tableOfTerrible: List<ChanceOfSuccess> = computeTableOfTerrible(allTokens, referenceCard, chaosBagVariableModifierValues)
        val chaosTokenVariableKeys: List<String> = referenceCard
            ?.tokens
            ?.map { it.modifierValue }
            ?.filterIsInstance<ChaosTokenModifierValue.Variable>()
            ?.map { it.key }
            ?: emptyList()

        companion object {
            private fun computeTableOfTerrible(
                allTokens: List<ChaosToken>,
                referenceCard: ScenarioReferenceCard?,
                variableValues: Map<String, Int>,
            ): List<ChanceOfSuccess> {
                val advantageList: List<Int> = if (referenceCard == null) {
                    // compute probabilities from -2 to +4, ignoring non-number tokens
                    (-2..4).toList()
                } else {
                    // compute probabilities from -2 to +4, but also include the min/max modifiers from the reference
                    // card. Attempt to compute the probabilities, accounting for the known "special" token values
                    val allTokenValues = referenceCard
                        .tokens
                        .mapNotNull { it.token.modifierValue(referenceCard, variableValues) }
                    val minValue = allTokenValues.minOrNull()
                    val maxValue = allTokenValues.minOrNull()

                    buildList {
                        this += (-2..4).toList()
                        if (minValue != null) this += minValue
                        if (maxValue != null) this += maxValue
                    }
                }.distinct().sorted()

                return advantageList
                    .map {
                        val numberOfTokensThatFail = findNumberOfTokensToCauseFailure(allTokens, referenceCard, variableValues, it)
                        ChanceOfSuccess(
                            advantage = it,
                            numberOfTokensThatFail = numberOfTokensThatFail,
                            chanceOfSuccess = (allTokens.size - numberOfTokensThatFail).toDouble() / allTokens.size,
                        )
                    }
            }

            private fun findNumberOfTokensToCauseFailure(
                allTokens: List<ChaosToken>,
                referenceCard: ScenarioReferenceCard?,
                variableValues: Map<String, Int>,
                advantage: Int,
            ): Int {
                return allTokens
                    .map { it.modifierValue(referenceCard, variableValues) ?: 0 }
                    .count { (it + advantage) < 0 }
            }
        }
    }

    sealed class Inputs {
        object InitializeDefault : Inputs()
        data class InitializeForScenario(val scenarioId: ScenarioId) : Inputs()
        data class AddTokenToBag(val token: ChaosToken) : Inputs()
        data class RemoveTokenFromBag(val token: ChaosToken) : Inputs()
        data class ChangeScenarioDifficulty(val difficulty: ScenarioDifficulty) : Inputs()

        data class UpdateTokenModifierValue(val key: String, val value: Int) : Inputs()

        object DrawToken : Inputs()
        data class PutTokenBack(val token: ChaosToken) : Inputs()
        object PutAllTokensBack : Inputs()

        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
    }

    sealed class Events
}
