package com.caseyjbrooks.arkham.repository.main

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

class ArkhamExplorerInputHandler : InputHandler<
    ArkhamExplorerContract.Inputs,
    Any,
    ArkhamExplorerContract.State> {
    override suspend fun InputHandlerScope<
        ArkhamExplorerContract.Inputs,
        Any,
        ArkhamExplorerContract.State>.handleInput(
        input: ArkhamExplorerContract.Inputs
    ) = when (input) {
        is ArkhamExplorerContract.Inputs.FetchCachedValue -> {
            val currentState = getCurrentState()

            val cachedValue = currentState.caches.singleOrNull { it.key == input.key }
            if(cachedValue != null) {
                // refresh the cache
                cachedValue.fetchWithCache(input.forceRefresh)
            } else {
                // add the cache and start it running
                val newCachedValue = input.newCache()
                newCachedValue.fetchWithCache(input.forceRefresh)
                updateState { it.copy(caches = (it.caches + newCachedValue).takeLast(10)) }
            }
        }
    }
}
