package com.caseyjbrooks.arkham.ui.tools.chaosbag

import com.caseyjbrooks.arkham.utils.removeAllPreservingDuplicates
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.ChaosBagDifficulty
import com.copperleaf.arkham.models.api.ChaosToken
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ScenarioDetails
import com.copperleaf.arkham.models.api.ScenarioId
import com.copperleaf.arkham.random.ImmutableRandom
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.datetime.Clock

object ChaosBagSimulatorContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),
        val random: ImmutableRandom = ImmutableRandom(Clock.System.now().toEpochMilliseconds()),

        val scenarioId: ScenarioId? = null,
        val scenario: ScenarioDetails? = null,
        val selectedDifficulty: ChaosBagDifficulty = ChaosBagDifficulty.Standard,

        val tokens: List<ChaosToken> = emptyList(),
        val consumedTokens: List<ChaosToken> = emptyList(),
    ) {
        val remainingTokens: List<ChaosToken> = tokens.removeAllPreservingDuplicates(consumedTokens)
    }

    sealed class Inputs {
        object InitializeDefault : Inputs()
        data class InitializeForScenario(val scenarioId: ScenarioId) : Inputs()
        data class AddTokenToBag(val token: ChaosToken) : Inputs()
        data class RemoveTokenFromBag(val token: ChaosToken) : Inputs()
        data class ChangeScenarioDifficulty(val difficulty: ChaosBagDifficulty) : Inputs()

        object DrawToken : Inputs()
        data class PutTokenBack(val token: ChaosToken) : Inputs()
        object PutAllTokensBack : Inputs()

        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
    }

    sealed class Events
}
