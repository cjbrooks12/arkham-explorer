package com.caseyjbrooks.arkham.repository.main

import com.caseyjbrooks.arkham.config.ArkhamConfig
import com.caseyjbrooks.arkham.models.ArkhamHorrorExpansion
import com.caseyjbrooks.arkham.utils.resourceloader.ResourceLoader
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import kotlinx.serialization.json.Json

class ArkhamExplorerInputHandler(
    private val config: ArkhamConfig,
    private val resourceLoader: ResourceLoader,
) : InputHandler<
    ArkhamExplorerContract.Inputs,
    ArkhamExplorerContract.Events,
    ArkhamExplorerContract.State> {
    override suspend fun InputHandlerScope<
        ArkhamExplorerContract.Inputs,
        ArkhamExplorerContract.Events,
        ArkhamExplorerContract.State>.handleInput(
        input: ArkhamExplorerContract.Inputs
    ) = when (input) {
        is ArkhamExplorerContract.Inputs.Initialize -> {
            sideJob("load expansions") {
                val loadedExpansions = config.getExpansions().map { expansionFileName ->
                    Json.decodeFromString(
                        ArkhamHorrorExpansion.serializer(),
                        resourceLoader.load(expansionFileName)
                    )
                }
                postInput(
                    ArkhamExplorerContract.Inputs.ExpansionsLoaded(loadedExpansions)
                )
            }
        }
        is ArkhamExplorerContract.Inputs.ExpansionsLoaded -> {
            updateState { it.copy(ready = true, expansions = input.expansions) }
        }
    }
}
