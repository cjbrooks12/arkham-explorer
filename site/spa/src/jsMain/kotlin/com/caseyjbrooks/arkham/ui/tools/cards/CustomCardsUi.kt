package com.caseyjbrooks.arkham.ui.tools.cards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.CacheReady
import com.caseyjbrooks.arkham.utils.DynamicGrid
import com.caseyjbrooks.arkham.utils.GridItem
import com.caseyjbrooks.arkham.utils.canvas.CanvasContract
import com.caseyjbrooks.arkham.utils.canvas.ComposableCanvas
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.forms.compose.bulma.form.BulmaForm
import org.jetbrains.compose.web.dom.Text

@Suppress("UNUSED_PARAMETER")
object CustomCardsUi {
    @Composable
    fun Page(injector: ArkhamInjector) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.customCardsViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()
        Page(injector, vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(
        injector: ArkhamInjector,
        state: CustomCardsContract.State,
        postInput: (CustomCardsContract.Inputs) -> Unit
    ) {
        MainLayout(state.layout) {
            Header()
            Body(injector, state)
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
    fun Body(injector: ArkhamInjector, state: CustomCardsContract.State) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.canvasViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()

        DynamicGrid(
            GridItem("is-8") {
                Card(title = "Investigators") {
                    CacheReady(vmState.formDefinition) { form ->
                        BulmaForm(
                            schema = form.schema,
                            uiSchema = form.uiSchema,
                            data = vmState.formData,
                            onDataChanged = { vm.trySend(CanvasContract.Inputs.FormDataUpdated(it)) }
                        )
                    }
                }
            },
            GridItem("is-4") {
                Card(title = "Scenarios") {
                    ComposableCanvas(vmState)
                }
            }
        )
    }
}
