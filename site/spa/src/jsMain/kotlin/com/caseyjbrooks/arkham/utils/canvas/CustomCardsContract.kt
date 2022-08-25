package com.caseyjbrooks.arkham.utils.canvas

import com.copperleaf.arkham.models.api.ExpansionList
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

object CustomCardsContract {
    data class State(
        val width: Int = 0,
        val height: Int = 0,
        val data: JsonElement = JsonNull,

        val regions: Map<String, CanvasRegion<*>> = emptyMap(),
        val canvasState: Map<String, Any> = emptyMap(),

        val debug: Boolean = false,
    )

    sealed class Inputs {
        object Initialize : Inputs()
        data class ExpansionsUpdated(val expansions: Cached<ExpansionList>) : Inputs()
    }

    sealed class Events
}
