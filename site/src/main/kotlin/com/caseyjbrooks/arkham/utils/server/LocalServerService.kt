package com.caseyjbrooks.arkham.utils.server

import com.caseyjbrooks.arkham.utils.index.IndexService
import kotlinx.coroutines.coroutineScope

class LocalServerService(
    private val port: Int,
) {
    private val server: LocalServer = LocalServer()

    suspend fun startServer(indexService: IndexService) = coroutineScope {
        with(server) { start(indexService.index, port) }
    }
}
