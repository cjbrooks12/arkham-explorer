package com.caseyjbrooks.arkham.ui.tools.campaignlog

import com.copperleaf.arkham.models.api.ScenarioId
import com.copperleaf.ballast.savedstate.RestoreStateScope
import com.copperleaf.ballast.savedstate.SaveStateScope
import com.copperleaf.ballast.savedstate.SavedStateAdapter
import com.copperleaf.json.pointer.toKotlinxJsonValue
import com.copperleaf.json.utils.parseJson
import com.copperleaf.json.utils.toJsonString
import kotlinx.browser.window
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import org.w3c.dom.get
import org.w3c.dom.set

class CampaignLogSavedStateAdapter(
    private val expansionCode: String?,
    private val campaignLogId: String?,
) : SavedStateAdapter<
    CampaignLogContract.Inputs,
    CampaignLogContract.Events,
    CampaignLogContract.State,
    > {

    private val campaignId = campaignLogId ?: expansionCode ?: "anonymous campaign"

    override suspend fun SaveStateScope<
        CampaignLogContract.Inputs,
        CampaignLogContract.Events,
        CampaignLogContract.State>.save() {
        this.saveAll {
            val scenariosList: JsonElement = it.scenarioIds.map { it.id }.toKotlinxJsonValue()
            val scenariosData: JsonElement = it.scenarioFormData
            val investigatorsData: JsonElement = it.investigatorFormData

            window.localStorage["scenarios[$campaignId]"] = scenariosList.toJsonString(false)
            window.localStorage["scenariosData[$campaignId]"] = scenariosData.toJsonString(false)
            window.localStorage["investigatorsData[$campaignId]"] = investigatorsData.toJsonString(false)
        }
    }

    override suspend fun RestoreStateScope<
        CampaignLogContract.Inputs,
        CampaignLogContract.Events,
        CampaignLogContract.State>.restore(): CampaignLogContract.State {
        val scenariosList: List<String> = window.localStorage["scenarios[$campaignId]"]
            ?.parseJson()
            ?.jsonArray
            ?.map { it.jsonPrimitive.content }
            ?: emptyList()
        val scenariosData: JsonElement = window.localStorage["scenariosData[$campaignId]"]
            ?.parseJson() ?: JsonNull
        val investigatorsData: JsonElement = window.localStorage["investigatorsData[$campaignId]"]
            ?.parseJson() ?: JsonNull
        return CampaignLogContract.State(
            scenarioIds = scenariosList.map { ScenarioId(it) }.toSet(),
            scenarioFormData = scenariosData,
            investigatorFormData = investigatorsData,
        )
    }

    override suspend fun onRestoreComplete(restoredState: CampaignLogContract.State): CampaignLogContract.Inputs {
        println("on restore complete")
        return CampaignLogContract.Inputs.Initialize(expansionCode, campaignLogId)
    }
}
