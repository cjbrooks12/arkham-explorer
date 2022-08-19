package com.caseyjbrooks.arkham.ui.tools.cards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.caseyjbrooks.arkham.app.BuildConfig
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.ui.LocalInjector
import com.caseyjbrooks.arkham.utils.DynamicGrid
import com.caseyjbrooks.arkham.utils.GridItem
import com.caseyjbrooks.arkham.utils.form.CustomCardDataStore
import com.caseyjbrooks.arkham.utils.form.FormDoubleRegion
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.forms.compose.bulma.form.BulmaForm
import com.copperleaf.forms.core.vm.BasicFormViewModel
import com.copperleaf.forms.core.vm.FormContract
import com.copperleaf.forms.core.vm.FormSavedStateAdapter
import com.copperleaf.json.values.arrayAt
import com.copperleaf.json.values.optional
import com.copperleaf.json.values.string
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.compose.web.dom.Canvas
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.CENTER
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextAlign
import org.w3c.dom.CanvasTextBaseline
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Image
import org.w3c.dom.MIDDLE

@Suppress("UNUSED_PARAMETER")
object CustomCardsUi {
    @Composable
    fun Page(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.customCardsViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: CustomCardsContract.State, postInput: (CustomCardsContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            Header()
            Body()
        }
    }

    @Composable
    fun Header() {
        Hero(
            title = { Text("Custom Cards Designer") },
            subtitle = { Text("Tools") },
            size = BulmaSize.Small,
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ArkhamApp.Home),
                NavigationRoute("Tools", null, ArkhamApp.Tools),
                NavigationRoute("Custom Card Designer", null, ArkhamApp.CustomCards),
            )
        }
    }

    @Composable
    fun Body() {
        val injector = LocalInjector.current
        val dataStore = remember(injector) {
            CustomCardDataStore(
                injector.httpClient,
                "assets",
                "assets"
            )
        }
        val regions: Map<String, FormDoubleRegion> by produceState(emptyMap(), dataStore) {
            value = dataStore
                .getFormRegions()
                .mapValues { FormDoubleRegion(it.value) }
        }

        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope) {
            BasicFormViewModel(
                coroutineScope,
                FormSavedStateAdapter(dataStore) {
                    FormContract.State(
                        saveType = FormContract.SaveType.OnAnyChange,
                        validationMode = FormContract.ValidationMode.NoValidation,
                        debug = false,
                    )
                }
            )
        }
        val vmState by vm.observeStates().collectAsState()

        val cardType = vmState.updatedData.optional { string("type") } ?: "asset"
        val cardClass = vmState.updatedData.optional { string("class") } ?: "guardian"

        DynamicGrid(
            GridItem {
                Card(title = "Form") {
                    BulmaForm(vm)
                }
            },
            GridItem {
                ComposableCanvas(
                    "${BuildConfig.BASE_URL}/assets/cards/$cardType/$cardClass.jpeg",
                    regions,
                    vmState
                ) { _, _ ->
                    textBaseline = CanvasTextBaseline.MIDDLE
                    textAlign = CanvasTextAlign.CENTER

                    regions.forEach { (regionPropertyName, region) ->
                        val regionPropertyValue = vmState.updatedData.optional { string(regionPropertyName) }
                            ?: vmState.updatedData
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

                        if (vmState.debug) {
                            strokeStyle = "red"
                            strokeRect(region.x, region.y, region.width, region.height)
                        }

                        strokeStyle = region.stroke
                        fillStyle = region.color
                        font = region.font
                        fillText(regionPropertyValue, region.centerX, region.centerY, region.width)
                    }
                }
            },
        )
    }

    @Composable
    fun ComposableCanvas(
        backgroundImageUrl: String,
        vararg keys: Any?,
        onDraw: CanvasRenderingContext2D.(width: Double, height: Double) -> Unit
    ) {
        val imageBitmap: Image? by produceState<Image?>(null, backgroundImageUrl) {
            val flow = callbackFlow<Image?> {
                val image = Image()
                image.src = backgroundImageUrl
                image.onload = { trySend(image) }
                awaitClose { image.onload = null }
            }

            flow
                .onEach { value = it }
                .launchIn(this)
        }
        var canvasEl: HTMLCanvasElement? by remember { mutableStateOf(null) }
        var drawScope: CanvasRenderingContext2D? by remember { mutableStateOf(null) }

        LaunchedEffect(drawScope, imageBitmap, canvasEl, *keys) {
            imageBitmap?.let { bmp ->
                canvasEl?.let { canvas ->
                    drawScope?.apply {
                        canvas.width = bmp.naturalWidth
                        canvas.height = bmp.naturalHeight
                        drawImage(bmp, 0.0, 0.0, bmp.width.toDouble(), bmp.height.toDouble())
                        onDraw(canvas.width.toDouble(), canvas.height.toDouble())
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
}
