package com.caseyjbrooks.arkham.dag.updater

import com.caseyjbrooks.arkham.dag.DependencyGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

/**
 * Continuously emit changes to the dependency graph, so it repeatedly rebuilds itself without actually tracking files
 * on disk.
 */
class ConstantUpdaterService(private val period: Duration) : UpdaterService {

    override fun watchForChanges(graph: DependencyGraph): Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

}
