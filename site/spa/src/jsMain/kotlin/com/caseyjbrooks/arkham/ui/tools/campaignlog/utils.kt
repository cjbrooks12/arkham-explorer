package com.caseyjbrooks.arkham.ui.tools.campaignlog

import com.copperleaf.arkham.models.api.ScenarioLite
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.ballast.repository.cache.isLoading
import com.copperleaf.ballast.repository.cache.map

fun CampaignLogContract.State.getAvailableScenarios(): Cached<List<ScenarioLite>> {
    return layout.map { layout ->
        val selectedScenarioIds = scenarios.map { it.first.id }
        if (scenarios.isEmpty()) {
            // show a start scenario to select
            layout
                .expansions
                .flatMap { it.startScenario }
                .filter {
                    it.id !in selectedScenarioIds
                }
        } else if (expansionCode == "standalone") {
            // show all the standalone scenarios
            val selectedExpansionScenarios = expansion.getCachedOrNull()?.scenarios ?: emptyList()
            selectedExpansionScenarios
                .filter {
                    it.id !in selectedScenarioIds
                }
        } else {
            // show the other scenarios
            val selectedExpansionScenarios = expansion.getCachedOrNull()?.scenarios ?: emptyList()
            val standaloneScenarios = standaloneExpansions.getCachedOrNull()?.scenarios ?: emptyList()

            val availableScenarios = buildList<ScenarioLite> {
                if (currentScenarioDetails != null && !currentScenarioDetails.isLoading()) {
                    val scenarioDetails = currentScenarioDetails.getCachedOrThrow()
                    this += selectedExpansionScenarios.filter {
                        it.id in scenarioDetails.nextScenario
                    }
                } else {
                    this += selectedExpansionScenarios
                }

                this += standaloneScenarios
            }

            availableScenarios
                .filter {
                    it.id !in selectedScenarioIds
                }
        }
    }
}
