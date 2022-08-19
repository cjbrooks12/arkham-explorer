package com.caseyjbrooks.arkham.utils.form

import com.copperleaf.forms.core.vm.FormSavedStateAdapter
import com.copperleaf.json.utils.parseJson
import com.copperleaf.json.utils.toJsonString
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.browser.window
import kotlinx.serialization.json.JsonElement
import org.w3c.dom.get
import org.w3c.dom.set

class CustomCardDataStore(
    private val httpClient: HttpClient,
    private val formType: String,
    private val localStorageKey: String,
) : FormSavedStateAdapter.Store {
    override suspend fun loadSchema(): JsonElement {
        return httpClient.get("schemas/$formType/schema.json").body()
    }

    override suspend fun loadUiSchema(): JsonElement {
        return httpClient.get("schemas/$formType/uiSchema.json").body()
    }

    override suspend fun loadInitialData(): JsonElement {
        return window.localStorage.get(localStorageKey)?.parseJson()
            ?: httpClient.get("schemas/$formType/defaultData.json").body()
    }

    override suspend fun saveUpdatedData(data: JsonElement) {
        window.localStorage.set(localStorageKey, data.toJsonString())
    }

    suspend fun getFormRegions(): Map<String, FormIntRegion> {
        return httpClient.get("schemas/$formType/regions.json").body()
    }
}
