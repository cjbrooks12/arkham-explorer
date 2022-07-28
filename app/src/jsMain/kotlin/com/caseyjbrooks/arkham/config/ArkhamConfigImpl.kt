package com.caseyjbrooks.arkham.config

class ArkhamConfigImpl : ArkhamConfig {
    override val imageCacheSize: Int = 10

    override suspend fun getExpansions(): List<String> = listOf(
        "/assets/expansions/night-of-the-zealot/data.json",
        "/assets/expansions/return-to-night-of-the-zealot/data.json",
        "/assets/expansions/the-dunwich-legacy/data.json",
        "/assets/expansions/return-to-the-dunwich-legacy/data.json",
    )
}
