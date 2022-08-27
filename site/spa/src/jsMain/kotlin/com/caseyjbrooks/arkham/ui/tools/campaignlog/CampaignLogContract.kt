package com.caseyjbrooks.arkham.ui.tools.campaignlog

import com.caseyjbrooks.arkham.utils.form.FormDefinition
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayoutState
import com.copperleaf.arkham.models.api.Expansion
import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.arkham.models.api.ScenarioDetails
import com.copperleaf.arkham.models.api.ScenarioId
import com.copperleaf.arkham.models.api.ScenarioLite
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.map
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

object CampaignLogContract {
    data class State(
        val layout: Cached<MainLayoutState> = Cached.NotLoaded(),

        val expansionCode: String? = null,
        val campaignLogId: String? = null,
        val expansion: Cached<Expansion> = Cached.NotLoaded(),
        val standaloneExpansions: Cached<Expansion> = Cached.NotLoaded(),

        val investigatorsFormDefinition: Cached<FormDefinition> = Cached.NotLoaded(),
        val investigatorFormData: JsonElement = JsonObject(emptyMap()),

        val scenarioIds: Set<ScenarioId> = emptySet(),
        val scenarios: List<Pair<ScenarioLite, Cached<ScenarioDetails>>> = emptyList(),
        val currentScenarioId: ScenarioId? = null,
        val scenarioFormData: JsonElement = JsonObject(emptyMap()),
    ) {
        val currentScenario: ScenarioLite? = scenarios
            .firstOrNull { it.first.id == currentScenarioId }
            ?.first
        val currentScenarioDetails: Cached<ScenarioDetails>? = scenarios
            .firstOrNull { it.first.id == currentScenarioId }
            ?.second
        val scenarioFormDefinition: Cached<FormDefinition>? = currentScenarioDetails?.map {
            val schema = JsonSchema.parse(it.campaignLogSchema)
            val uiSchema = UiSchema.parse(schema, it.campaignLogUiSchema)
            FormDefinition(
                schema = schema,
                uiSchema = uiSchema,
                defaultData = JsonObject(emptyMap()),
            )
        }

        val availableScenarios: Cached<List<ScenarioLite>> = getAvailableScenarios()
    }

    sealed class Inputs {
        data class Initialize(val expansionCode: String?, val campaignLogId: String?) : Inputs()

        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
        data class ExpansionUpdated(val expansion: Cached<Expansion>) : Inputs()
        data class StandaloneExpansionsUpdated(val expansion: Cached<Expansion>) : Inputs()

        data class ScenarioDetailsUpdated(val scenarioId: ScenarioId, val scenarioDetails: Cached<ScenarioDetails>) :
            Inputs()

        data class InvestigatorsFormUpdated(val formDefinition: Cached<FormDefinition>) : Inputs()
        data class InvestigatorsFormDataUpdated(val formData: JsonElement) : Inputs()
        data class ScenarioFormDataUpdated(val formData: JsonElement) : Inputs()

        data class ChangeSelectedTab(val scenarioId: ScenarioId) : Inputs()
        data class AddScenario(val scenario: ScenarioLite) : Inputs()
        data class RemoveScenario(val scenarioId: ScenarioId) : Inputs()
    }

    sealed class Events
}
