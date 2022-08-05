package com.caseyjbrooks.arkham.dag.updater

import com.caseyjbrooks.arkham.dag.DependencyGraph
import kotlinx.coroutines.flow.Flow

interface UpdaterService {

    fun watchForChanges(graph: DependencyGraph): Flow<Unit>

}
