package com.caseyjbrooks.arkham.utils.generator

import com.caseyjbrooks.arkham.stages.ProcessingStage
import com.caseyjbrooks.arkham.utils.cache.Cacheable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GeneratorService(
    vararg val stages: ProcessingStage
) {
    suspend fun getInputEntries(): Iterable<Cacheable.Input<*, *>> = coroutineScope {
        stages
            .map { async { it.process() } }
            .awaitAll()
            .flatten()
    }
}
