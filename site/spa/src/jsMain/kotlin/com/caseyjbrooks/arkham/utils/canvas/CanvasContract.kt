package com.caseyjbrooks.arkham.utils.canvas

import com.caseyjbrooks.arkham.utils.form.FormDefinition
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.isLoading
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

object CanvasContract {
    data class State(
        val formDefinition: Cached<FormDefinition> = Cached.NotLoaded(),
        val formData: JsonElement = JsonNull,

        val canvasDefinition: Cached<CanvasDefinition> = Cached.NotLoaded(),
        val canvasData: Map<String, CanvasValue> = emptyMap(),
    ) {
        val isLoading = formDefinition.isLoading() || canvasDefinition.isLoading()
    }

    sealed class Inputs {
        object Initialize : Inputs()

        data class FormDefinitionUpdated(val formDefinition: Cached<FormDefinition>) : Inputs()
        data class FormDataUpdated(val formData: JsonElement) : Inputs()

        data class CanvasDefinitionUpdated(val canvasDefinition: Cached<CanvasDefinition>) : Inputs()
        data class CanvasDataUpdated(val region: String, val value: CanvasValue) : Inputs()
    }

    sealed class Events
}
