package com.caseyjbrooks.arkham.utils.canvas

import kotlinx.serialization.Serializable

@Serializable
class CanvasDefinition(
    val width: Int,
    val height: Int,
    val regions: Map<String, CanvasRegion>,
)
