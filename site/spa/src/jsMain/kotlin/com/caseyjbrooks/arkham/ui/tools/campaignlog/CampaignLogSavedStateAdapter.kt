package com.caseyjbrooks.arkham.ui.tools.campaignlog

import com.copperleaf.ballast.savedstate.RestoreStateScope
import com.copperleaf.ballast.savedstate.SaveStateScope
import com.copperleaf.ballast.savedstate.SavedStateAdapter
import com.copperleaf.json.pointer.toKotlinxJsonValue
import com.copperleaf.json.utils.toJsonString
import kotlinx.browser.window
import kotlinx.serialization.json.JsonElement
import org.w3c.dom.set

class CampaignLogSavedStateAdapter : SavedStateAdapter<
    CampaignLogContract.Inputs,
    CampaignLogContract.Events,
    CampaignLogContract.State,
    > {

    override suspend fun SaveStateScope<
        CampaignLogContract.Inputs,
        CampaignLogContract.Events,
        CampaignLogContract.State>.save() {
        this.saveAll {
            val campaignId = it.campaignLogId ?: it.expansionCode ?: "anonymous campaign"
            val scenariosList: JsonElement = it.scenarios.map { it.first.id.id }.toKotlinxJsonValue()
            val scenariosData: JsonElement = it.scenarioFormData
            val investigatorsData: JsonElement = it.investigatorFormData

            window.localStorage.set("scenarios[$campaignId]", scenariosList.toJsonString(false))
            window.localStorage.set("scenariosData[$campaignId]", scenariosData.toJsonString(false))
            window.localStorage.set("investigatorsData[$campaignId]", investigatorsData.toJsonString(false))
        }
    }

    override suspend fun RestoreStateScope<
        CampaignLogContract.Inputs,
        CampaignLogContract.Events,
        CampaignLogContract.State>.restore(): CampaignLogContract.State {
        return CampaignLogContract.State()
    }
}
