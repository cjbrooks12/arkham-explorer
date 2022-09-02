package com.caseyjbrooks.arkham.utils.canvas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.json.values.boolean
import com.copperleaf.json.values.optional
import org.jetbrains.compose.web.attributes.height
import org.jetbrains.compose.web.attributes.width
import org.jetbrains.compose.web.dom.Canvas
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement


@Composable
fun ComposableCanvas(
    state: CanvasContract.State
) {
    var canvasEl: HTMLCanvasElement? by remember { mutableStateOf(null) }
    var drawScope: CanvasRenderingContext2D? by remember { mutableStateOf(null) }

    LaunchedEffect(canvasEl, drawScope, state) {
        canvasEl?.let { canvas ->
            drawScope?.let { drawScope2d ->
                state.canvasDefinition.getCachedOrNull()?.let { canvasDefinition ->
                    val sortedRegions = state.canvasDefinition.getCachedOrNull()
                        ?.regions
                        ?.entries
                        ?.filter { (key, _) -> state.canvasData.containsKey(key) }
                        ?.sortedBy { (_, region) -> region.zIndex }
                        ?: emptyList()

                    sortedRegions.forEach { (regionPropertyName, region) ->
                        val context = CanvasRegionContext(
                            canvasContext = drawScope2d,
                            canvasWidth = canvasDefinition.width,
                            canvasHeight = canvasDefinition.height,
                            debug = state.formData.optional { boolean("debug") } ?: false,
                        )

                        val regionValue = state.canvasData[regionPropertyName]

                        regionValue?.let { regionValueNotNull ->
                            with(region) { context.onDraw(regionValueNotNull) }
                        }
                    }
                }
            }
        }
    }

    Canvas(
        {
            state.canvasDefinition.getCachedOrNull()?.let { canvasDefinition ->
                width(canvasDefinition.width)
                height(canvasDefinition.height)
            }
            ref {
                canvasEl = it
                drawScope = it.getContext("2d") as CanvasRenderingContext2D

                onDispose {
                    canvasEl = null
                    drawScope = null
                }
            }
        }
    )
}
