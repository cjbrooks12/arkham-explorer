package com.caseyjbrooks.arkham.stages

import com.caseyjbrooks.arkham.utils.cache.Cacheable

interface ProcessingStage {

    suspend fun process(): Iterable<Cacheable.Input<*, *>>
}
