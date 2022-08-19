package com.caseyjbrooks.arkham.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.ui.ArkhamApp
import com.caseyjbrooks.arkham.utils.CacheReady
import com.caseyjbrooks.arkham.utils.theme.bulma.Breadcrumbs
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSection
import com.caseyjbrooks.arkham.utils.theme.bulma.BulmaSize
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.arkham.models.api.StaticPage
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Suppress("UNUSED_PARAMETER")
object StaticPageUi {
    @Composable
    fun Page(injector: ArkhamInjector, slug: String) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector, slug) { injector.staticPageViewModel(coroutineScope, slug) }
        val vmState by vm.observeStates().collectAsState()
        Page(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Page(state: StaticPageContract.State, postInput: (StaticPageContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            CacheReady(state.content) { page ->
                Header(page)
            }
        }
    }

    @Composable
    fun Header(page: StaticPage) {
        Hero(
            title = { Text(page.title) },
            size = BulmaSize.Medium,
        )
        BulmaSection {
            Breadcrumbs(
                NavigationRoute("Home", null, ArkhamApp.Home),
                NavigationRoute(page.title, null, ArkhamApp.StaticPage, page.slug),
            )
        }
    }

    @Composable
    fun Body(page: StaticPage) {
        BulmaSection {
            Card(
                content = {
                    Div(attrs = {
                        ref { element ->
                            element.innerHTML = page.content
                            onDispose {}
                        }
                    })
                }
            )
        }
    }
}
