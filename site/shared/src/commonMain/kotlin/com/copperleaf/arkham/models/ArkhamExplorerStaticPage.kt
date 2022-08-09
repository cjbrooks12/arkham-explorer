package com.copperleaf.arkham.models

import kotlinx.serialization.Serializable

@Serializable
data class ArkhamExplorerStaticPage(
    val title: String = "",
    val slug: String = "",
    val content: String = "",
)
