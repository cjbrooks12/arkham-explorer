package com.caseyjbrooks.arkham.utils.canvas

import kotlinx.serialization.json.JsonElement

class CanvasValue(
    val value: Any,
    val dependencies: Map<String, JsonElement>,
)
