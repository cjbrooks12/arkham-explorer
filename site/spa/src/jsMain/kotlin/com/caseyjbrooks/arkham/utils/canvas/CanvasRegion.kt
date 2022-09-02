package com.caseyjbrooks.arkham.utils.canvas

import com.caseyjbrooks.arkham.app.BuildConfig
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
import kotlinx.serialization.json.jsonObject
import org.w3c.dom.CENTER
import org.w3c.dom.CanvasTextAlign
import org.w3c.dom.CanvasTextBaseline
import org.w3c.dom.MIDDLE
import kotlin.coroutines.resume
import org.w3c.dom.Image as DomImage

@Serializable
sealed class CanvasRegion {

    abstract val zIndex: Int

    abstract suspend fun produceValue(
        regionPropertyName: String,
        regionData: JsonElement,
        previousValue: CanvasValue?
    ): CanvasValue

    abstract fun CanvasRegionContext.onDraw(regionValue: CanvasValue)

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
    ) : CanvasRegion() {
        private val centerX: Double = x + (width / 2.0)
        private val centerY: Double = y + (height / 2.0)

        private fun determineTextDependencies(
            regionPropertyName: String,
            regionData: JsonElement
        ): Map<String, JsonElement> {
            return if (regionData is JsonObject) {
                val regionValue = regionData.jsonObject[regionPropertyName] ?: JsonNull
                mapOf(regionPropertyName to regionValue)
            } else {
                emptyMap()
            }
        }

        override suspend fun produceValue(
            regionPropertyName: String,
            regionData: JsonElement,
            previousValue: CanvasValue?
        ): CanvasValue {
            val dependencies = determineTextDependencies(regionPropertyName, regionData)
            return if (previousValue != null && dependencies == previousValue.dependencies) {
                // re-use the same cached value
                previousValue
            } else {
                // This is the first time loading data, or else the backing field has changed in the form. Recompute the
                // actual string from the input JSON
                val actualTextValue: String = regionData.optional { string(regionPropertyName) }
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

                CanvasValue(actualTextValue, dependencies)
            }
        }

        override fun CanvasRegionContext.onDraw(regionValue: CanvasValue): Unit = with(canvasContext) {
            val actualTextValue: String = regionValue.value as String

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
                actualTextValue,
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
        val urlParams: List<String> = emptyList(),
        override val zIndex: Int = 0,
    ) : CanvasRegion() {

        private fun determineTextDependencies(
            regionPropertyName: String,
            regionData: JsonElement
        ): Map<String, JsonElement> {
            return if (regionData is JsonObject) {
                val keys = listOf(regionPropertyName) + urlParams
                keys
                    .map { key -> key to (regionData.jsonObject[key] ?: JsonNull) }
                    .toMap()
            } else {
                emptyMap()
            }
        }

        private fun getFormattedUrl(dependencies: Map<String, JsonElement>): String {
            return "${BuildConfig.BASE_URL}$url"
        }

        override suspend fun produceValue(
            regionPropertyName: String,
            regionData: JsonElement,
            previousValue: CanvasValue?
        ): CanvasValue {
            val dependencies = determineTextDependencies(regionPropertyName, regionData)
            return if (previousValue != null && dependencies == previousValue.dependencies) {
                // re-use the same cached value
                previousValue
            } else {
                // This is the first time loading data, or else the backing field has changed in the form. Recompute the
                // actual string from the input JSON
                val actualDomImage = suspendCancellableCoroutine { cont ->
                    val image = DomImage()
                    image.src = getFormattedUrl(dependencies)
                    image.onload = { cont.resume(image) }
                    cont.invokeOnCancellation { image.onload = null }
                }

                CanvasValue(actualDomImage, dependencies)
            }
        }

        override fun CanvasRegionContext.onDraw(regionValue: CanvasValue): Unit = with(canvasContext) {
            val domImage = regionValue.value as DomImage
            drawImage(domImage, 0.0, 0.0, domImage.width.toDouble(), domImage.height.toDouble())
        }
    }
}
