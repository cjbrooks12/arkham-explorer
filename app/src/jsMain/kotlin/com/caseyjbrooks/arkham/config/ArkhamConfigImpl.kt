package com.caseyjbrooks.arkham.config

import com.caseyjbrooks.arkham.app.BuildConfig

class ArkhamConfigImpl : ArkhamConfig {
    override val imageCacheSize: Int = 10

    override val debug: Boolean = BuildConfig.DEBUG
    override val useHistoryApi: Boolean = true
    override val baseUrl: String = BuildConfig.BASE_URL
}
