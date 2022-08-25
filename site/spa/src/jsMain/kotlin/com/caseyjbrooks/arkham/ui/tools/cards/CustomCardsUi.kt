package com.caseyjbrooks.arkham.ui.tools.cards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import org.jetbrains.compose.web.dom.Text

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
//        val injector = LocalInjector.current
//        val dataStore = remember(injector) {
//            CustomCardDataStore(
//                injector.httpClient,
//                "assets",
//                "assets"
//            )
//        }
//        val regions: Map<String, CanvasRegion> by produceState(emptyMap(), dataStore) {
//            value = dataStore.getCanvasRegions()
//        }
//
//        val coroutineScope = rememberCoroutineScope()
//        val vm = remember(coroutineScope) {
//            BasicFormViewModel(
//                coroutineScope,
//                FormSavedStateAdapter(dataStore) {
//                    FormContract.State(
//                        saveType = FormContract.SaveType.OnAnyChange,
//                        validationMode = FormContract.ValidationMode.NoValidation,
//                        debug = false,
//                    )
//                }
//            )
//        }
//        val vmState by vm.observeStates().collectAsState()
//
//        val cardType = vmState.updatedData.optional { string("type") } ?: "asset"
//        val cardClass = vmState.updatedData.optional { string("class") } ?: "guardian"
//
//        DynamicGrid(
//            GridItem {
//                Card(title = "Form") {
//                    BulmaForm(vm)
//                }
//            },
//            GridItem {
//                ComposableCanvas(
//                    "${BuildConfig.BASE_URL}/assets/cards/$cardType/$cardClass.jpeg",
//                    regions,
//                    vmState.updatedData,
//                    vmState.debug
//                )
//            },
//        )
    }
}
