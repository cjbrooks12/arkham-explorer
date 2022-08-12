package com.caseyjbrooks.arkham.config

interface ArkhamConfig {
    val isPwa: Boolean
    val imageCacheSize: Int
    val debug: Boolean
    val useHistoryApi: Boolean
    val baseUrl: String
    val basePath: String?
}
