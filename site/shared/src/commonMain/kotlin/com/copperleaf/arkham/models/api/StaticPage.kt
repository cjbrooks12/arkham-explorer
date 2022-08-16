package com.copperleaf.arkham.models.api

import kotlinx.serialization.Serializable

@Serializable
data class StaticPage(
    val title: String = "",
    val slug: String = "",
    val content: String = "",
)
