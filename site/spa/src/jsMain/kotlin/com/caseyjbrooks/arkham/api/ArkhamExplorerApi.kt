package com.caseyjbrooks.arkham.api

import com.copperleaf.arkham.models.ArkhamHorrorExpansion

interface ArkhamExplorerApi {

    suspend fun getExpansions(): List<ArkhamHorrorExpansion>
}
