package com.caseyjbrooks.arkham.config

interface ArkhamConfig {
    val imageCacheSize: Int
    suspend fun getExpansions(): List<String>
}
