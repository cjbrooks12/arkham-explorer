package com.caseyjbrooks.arkham.api

import com.caseyjbrooks.arkham.models.ArkhamHorrorExpansion

interface ArkhamExplorerApi {

    suspend fun getExpansions(): List<ArkhamHorrorExpansion>
}
