package com.caseyjbrooks.arkham.ui.pages

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
import com.caseyjbrooks.arkham.utils.theme.bulma.Card
import com.caseyjbrooks.arkham.utils.theme.bulma.Hero
import com.caseyjbrooks.arkham.utils.theme.bulma.NavigationRoute
import com.caseyjbrooks.arkham.utils.theme.layouts.MainLayout
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

object StaticPageUi {
    @Composable
    fun Content(injector: ArkhamInjector, slug: String) {
        val coroutineScope = rememberCoroutineScope()
        val vm = remember(coroutineScope, injector) { injector.staticPageViewModel(coroutineScope, slug) }
        val vmState by vm.observeStates().collectAsState()
        Content(vmState) { vm.trySend(it) }
    }

    @Composable
    fun Content(state: StaticPageContract.State, postInput: (StaticPageContract.Inputs) -> Unit) {
        MainLayout(state.layout) {
            state.content.getCachedOrNull()?.let { page ->
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
                BulmaSection {
                    Card {
                        Div(attrs = {
                            ref { element ->
                                element.innerHTML = page.content
                                onDispose {}
                            }
                        })
                    }
                }
            }
        }
    }
}
