package com.caseyjbrooks.arkham.repository.main

import com.caseyjbrooks.arkham.api.ArkhamExplorerApi
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.bus.observeInputsFromBus
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.fetchWithCache

class ArkhamExplorerInputHandler(
    private val api: ArkhamExplorerApi,
    private val eventBus: EventBus,
) : InputHandler<
    ArkhamExplorerContract.Inputs,
    Any,
    ArkhamExplorerContract.State> {
    override suspend fun InputHandlerScope<
        ArkhamExplorerContract.Inputs,
        Any,
        ArkhamExplorerContract.State>.handleInput(
        input: ArkhamExplorerContract.Inputs
    ) = when (input) {
        is ArkhamExplorerContract.Inputs.Initialize -> {
            val previousState = getCurrentState()

            if (!previousState.initialized) {
                updateState { it.copy(initialized = true) }
                // start observing flows here
                logger.debug("initializing")
                observeFlows(
                    key = "Observe account changes",
                    eventBus
                        .observeInputsFromBus<ArkhamExplorerContract.Inputs>(),
                )
            } else {
                logger.debug("already initialized")
                noOp()
            }
        }

        is ArkhamExplorerContract.Inputs.RefreshExpansions -> {
            updateState { it.copy(expansionsInitialized = true) }
            fetchWithCache(
                input = input,
                forceRefresh = input.forceRefresh,
                getValue = { it.expansions },
                updateState = { ArkhamExplorerContract.Inputs.ExpansionsUpdated(it) },
                doFetch = { api.getExpansions() },
            )
        }

        is ArkhamExplorerContract.Inputs.ExpansionsUpdated -> {
            updateState { it.copy(expansions = input.expansions) }
        }

        is ArkhamExplorerContract.Inputs.RefreshStaticPageContent -> {
            updateState { it.copy(staticPageContentInitialized = it.staticPageContentInitialized + (input.slug to true)) }
            fetchWithCache(
                input = input,
                forceRefresh = input.forceRefresh,
                getValue = { it.staticPageContent[input.slug] ?: Cached.NotLoaded() },
                updateState = { ArkhamExplorerContract.Inputs.StaticPageContentUpdated(input.slug, it) },
                doFetch = { api.getStaticPageContent(input.slug) },
            )
        }

        is ArkhamExplorerContract.Inputs.StaticPageContentUpdated -> {
            updateState { it.copy(staticPageContent = it.staticPageContent + (input.slug to input.content)) }
        }
    }
}
