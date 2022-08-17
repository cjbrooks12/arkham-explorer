package com.caseyjbrooks.arkham.utils.form

import kotlinx.serialization.Serializable

@Serializable
data class FormIntRegion(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val font: String,
    val color: String = "black",
    val stroke: String = "black",
)
