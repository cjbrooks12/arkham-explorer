package com.caseyjbrooks.arkham.config

class ArkhamConfigImpl : ArkhamConfig {
    override val imageCacheSize: Int = 10

    override suspend fun getExpansions(): List<String> = listOf(
        "/ArkhamHorrorLCG/encounter-sets/night-of-the-zealot.json",
        "/ArkhamHorrorLCG/encounter-sets/night-of-the-zealot-return-to.json",
        "/ArkhamHorrorLCG/encounter-sets/the-dunwich-legacy.json",
        "/ArkhamHorrorLCG/encounter-sets/the-dunwich-legacy-return-to.json",
    )
}
