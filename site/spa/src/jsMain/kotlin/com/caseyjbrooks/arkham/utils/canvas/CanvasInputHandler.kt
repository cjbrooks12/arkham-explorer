package com.caseyjbrooks.arkham.utils.canvas

import com.caseyjbrooks.arkham.utils.form.FormDefinition
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CanvasInputHandler(
    private val loadFormDefinition: suspend () -> Flow<Cached<FormDefinition>>,
    private val loadCanvasDefinition: suspend () -> Flow<Cached<CanvasDefinition>>,
) : InputHandler<
    CanvasContract.Inputs,
    CanvasContract.Events,
    CanvasContract.State
    > {

    override suspend fun InputHandlerScope<CanvasContract.Inputs, CanvasContract.Events, CanvasContract.State>.handleInput(
        input: CanvasContract.Inputs
    ) = when (input) {
        is CanvasContract.Inputs.Initialize -> {
            observeFlows(
                "load definitions",
                loadFormDefinition().map { CanvasContract.Inputs.FormDefinitionUpdated(it) },
                loadCanvasDefinition().map { CanvasContract.Inputs.CanvasDefinitionUpdated(it) },
            )
        }

        is CanvasContract.Inputs.FormDefinitionUpdated -> {
            val currentState = updateStateAndGet { it.copy(formDefinition = input.formDefinition) }

            if (!currentState.isLoading) {
                reloadCanvasValues(currentState)
            }

            Unit
        }

        is CanvasContract.Inputs.FormDataUpdated -> {
            val currentState = updateStateAndGet { it.copy(formData = input.formData) }

            if (!currentState.isLoading) {
                reloadCanvasValues(currentState)
            }

            Unit
        }

        is CanvasContract.Inputs.CanvasDefinitionUpdated -> {
            val currentState = updateStateAndGet { it.copy(canvasDefinition = input.canvasDefinition) }

            if (!currentState.isLoading) {
                reloadCanvasValues(currentState)
            }

            Unit
        }

        is CanvasContract.Inputs.CanvasDataUpdated -> {
            updateState { it.copy(canvasData = it.canvasData + (input.region to input.value)) }
        }
    }

    private fun InputHandlerScope<CanvasContract.Inputs, CanvasContract.Events, CanvasContract.State>.reloadCanvasValues(
        currentState: CanvasContract.State
    ) {
        currentState.canvasDefinition.getCachedOrThrow().regions.forEach { (key, region) ->
            // reach region will be loading its data in its own side-job
            sideJob("load canvas data: $key") {
                val previousRegionValue = currentStateWhenStarted.canvasData[key]
                val newRegionValue = region.produceValue(key, currentStateWhenStarted.formData, previousRegionValue)
                postInput(
                    CanvasContract.Inputs.CanvasDataUpdated(key, newRegionValue)
                )
            }
        }
    }
}
