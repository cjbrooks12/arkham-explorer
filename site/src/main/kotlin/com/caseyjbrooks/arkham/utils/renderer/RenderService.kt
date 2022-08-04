package com.caseyjbrooks.arkham.utils.renderer

import com.caseyjbrooks.arkham.utils.cache.CacheService
import com.caseyjbrooks.arkham.utils.index.IndexService

class RenderService {

    suspend fun renderAllOutputs(cacheService: CacheService, indexService: IndexService) {
        cacheService.prepareOutputDirectories(indexService)
        cacheService.renderOutputFiles(indexService)
    }
}
