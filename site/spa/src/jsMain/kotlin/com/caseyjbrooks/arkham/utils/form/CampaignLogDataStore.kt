package com.caseyjbrooks.arkham.utils.form

import com.copperleaf.arkham.models.api.Expansion
import com.copperleaf.forms.core.vm.FormSavedStateAdapter
import com.copperleaf.json.utils.parseJson
import com.copperleaf.json.utils.toJsonString
import kotlinx.browser.window
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.w3c.dom.get
import org.w3c.dom.set

class CampaignLogDataStore(
    private val expansion: Expansion,
    private val localStorageKey: String,
) : FormSavedStateAdapter.Store {
    override suspend fun loadSchema(): JsonElement {
        return expansion.campaignLogSchema
    }

    override suspend fun loadUiSchema(): JsonElement {
        return expansion.campaignLogUiSchema
    }

    override suspend fun loadInitialData(): JsonElement {
        return window.localStorage.get(localStorageKey)?.parseJson()
            ?: JsonObject(emptyMap())
    }

    override suspend fun saveUpdatedData(data: JsonElement) {
        window.localStorage.set(localStorageKey, data.toJsonString())
    }
}
