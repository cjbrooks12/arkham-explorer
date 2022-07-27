package com.caseyjbrooks.arkham.repository.main

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

class ArkhamExplorerEventHandler : EventHandler<
    ArkhamExplorerContract.Inputs,
    ArkhamExplorerContract.Events,
    ArkhamExplorerContract.State> {
    override suspend fun EventHandlerScope<
        ArkhamExplorerContract.Inputs,
        ArkhamExplorerContract.Events,
        ArkhamExplorerContract.State>.handleEvent(
        event: ArkhamExplorerContract.Events
    ) { }
}
