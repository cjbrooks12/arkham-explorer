package com.caseyjbrooks.arkham.utils.canvas

import org.w3c.dom.CanvasRenderingContext2D

data class CanvasRegionContext(
    val canvasContext: CanvasRenderingContext2D,
    val canvasWidth: Int,
    val canvasHeight: Int,
    val debug: Boolean,
)
