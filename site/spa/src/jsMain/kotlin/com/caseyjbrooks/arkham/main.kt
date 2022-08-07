package com.caseyjbrooks.arkham

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.arkham.di.ArkhamInjector
import com.caseyjbrooks.arkham.di.ArkhamInjectorImpl
import com.caseyjbrooks.arkham.ui.MainApplication
import com.caseyjbrooks.arkham.utils.theme.ArkhamTheme
import kotlinx.browser.window
import org.jetbrains.compose.web.renderComposable
import org.w3c.workers.ServiceWorkerGlobalScope


external val self: ServiceWorkerGlobalScope

fun main() {
    try {
        window.addEventListener("load", {
            window.navigator.serviceWorker.register("/spa.js")
        })

        browserMain()
    } catch (t: Throwable) {
        self.addEventListener("install", { event ->
            console.log("Service Worker installed!")
        })
        self.addEventListener("activate", { event ->
            console.log("Service Worker is now active!")
        })

        serviceWorkerMain()
    }
}

fun browserMain() {
    renderComposable(rootElementId = "root") {
        val applicationScope = rememberCoroutineScope()
        val injector: ArkhamInjector = remember(applicationScope) { ArkhamInjectorImpl(applicationScope) }

        ArkhamTheme(injector) {
            MainApplication(injector)
        }
    }
}

fun serviceWorkerMain() {

}
