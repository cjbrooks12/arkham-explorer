package com.caseyjbrooks.arkham.utils.canvas

import com.copperleaf.json.values.arrayAt
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.w3c.dom.CENTER
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextAlign
import org.w3c.dom.CanvasTextBaseline
import org.w3c.dom.MIDDLE
import kotlin.coroutines.resume
import org.w3c.dom.Image as DomImage

data class CanvasRegionContext(
    val canvasContext: CanvasRenderingContext2D,
    val canvasWidth: Int,
    val canvasHeight: Int,
    val debug: Boolean,
)

@Serializable
sealed class CanvasRegion<T : Any> {

    abstract val zIndex: Int

    abstract suspend fun produceValue(regionPropertyName: String, regionData: JsonElement): T

    abstract fun CanvasRegionContext.onDraw(regionValue: T)

    @Serializable
    @SerialName("text")
    data class Text(
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int,
        val font: String,
        val color: String = "black",
        val stroke: String = "black",
        override val zIndex: Int = 0,
    ) : CanvasRegion<String>() {
        private val centerX: Double = x + (width / 2.0)
        private val centerY: Double = y + (height / 2.0)

        override suspend fun produceValue(regionPropertyName: String, regionData: JsonElement): String {
            return regionData.optional { string(regionPropertyName) }
                ?: regionData
                    .optional { arrayAt(regionPropertyName) }
                    ?.mapNotNull {
                        when (it) {
                            is JsonPrimitive -> {
                                if (it == JsonNull) {
                                    null
                                } else {
                                    it.content
                                }
                            }

                            is JsonObject -> {
                                null
                            }

                            is JsonArray -> {
                                null
                            }
                        }
                    }
                    ?.joinToString(separator = " ") { "$it." }
                ?: ""
        }

        override fun CanvasRegionContext.onDraw(regionValue: String): Unit = with(canvasContext) {
            val region = this@Text

            textBaseline = CanvasTextBaseline.MIDDLE
            textAlign = CanvasTextAlign.CENTER

            if (debug) {
                strokeStyle = "red"
                strokeRect(
                    region.x.toDouble(),
                    region.y.toDouble(),
                    region.width.toDouble(),
                    region.height.toDouble(),
                )
            }

            strokeStyle = region.stroke
            fillStyle = region.color
            font = region.font

            fillText(
                regionValue,
                region.centerX,
                region.centerY,
                region.width.toDouble(),
            )
        }
    }

    @Serializable
    @SerialName("image")
    data class Image(
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int,
        val url: String,
        override val zIndex: Int = 0,
    ) : CanvasRegion<DomImage>() {

        override suspend fun produceValue(regionPropertyName: String, regionData: JsonElement): DomImage {
            return suspendCancellableCoroutine { cont ->
                val image = DomImage()
                image.src = url
                image.onload = { cont.resume(image) }
                cont.invokeOnCancellation { image.onload = null }
            }
        }

        override fun CanvasRegionContext.onDraw(regionValue: DomImage): Unit = with(canvasContext) {
            drawImage(regionValue, 0.0, 0.0, regionValue.width.toDouble(), regionValue.height.toDouble())
        }
    }
}
