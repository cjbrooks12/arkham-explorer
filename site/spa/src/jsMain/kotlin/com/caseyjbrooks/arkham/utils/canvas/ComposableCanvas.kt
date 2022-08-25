package com.caseyjbrooks.arkham.utils.canvas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.serialization.json.JsonElement
import org.jetbrains.compose.web.dom.Canvas
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement


@Composable
fun ComposableCanvas(
    width: Int,
    height: Int,
    regions: Map<String, CanvasRegion<*>>,
    regionData: JsonElement,
    debug: Boolean,
) {
    var canvasEl: HTMLCanvasElement? by remember { mutableStateOf(null) }
    var drawScope: CanvasRenderingContext2D? by remember { mutableStateOf(null) }

    val canvasState: Map<String, Any> by produceState(emptyMap(), regions, regionData) {
        value = regions
            .entries
            .map { (regionKey, region) ->
                async { regionKey to region.produceValue(regionKey, regionData) }
            }
            .awaitAll()
            .toMap()
    }

    LaunchedEffect(canvasEl, drawScope, canvasState) {
        canvasEl?.let { canvas ->
            drawScope?.let { drawScope2d ->
                regions.forEach { (regionPropertyName, region) ->
                    val context = CanvasRegionContext(
                        canvasContext = drawScope2d,
                        canvasWidth = width,
                        canvasHeight = height,
                        debug = debug,
                    )

                    val regionValue = canvasState[regionPropertyName]

                    regionValue?.let { regionValueNotNull ->
//                        with(region) { context.onDraw(regionValueNotNull) }
                    }
                }
            }
        }
    }

    Canvas(
        {
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
