package com.caseyjbrooks.arkham.utils.form

data class FormDoubleRegion(
    val x: Double,
    val y: Double,
    val width: Double,
    val height: Double,
    val font: String,
    val color: String,
    val stroke: String,
) {
    constructor(formIntRegion: FormIntRegion) : this(
        x = formIntRegion.x.toDouble(),
        y = formIntRegion.y.toDouble(),
        width = formIntRegion.width.toDouble(),
        height = formIntRegion.height.toDouble(),
        font = formIntRegion.font,
        color = formIntRegion.color,
        stroke = formIntRegion.stroke,
    )

    val centerX: Double = x + (width / 2.0)
    val centerY: Double = y + (height / 2.0)
}
