package com.caseyjbrooks.arkham

import com.caseyjbrooks.arkham.app.BuildConfig
import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams


fun main() {
    try {
        window.addEventListener("load", {
            window.navigator.serviceWorker
                .register("${BuildConfig.BASE_URL}/spa.js")
                .then { res -> console.log("service worker registered") }
                .catch { err -> console.log("service worker not registered", err) }
        })

        val params = URLSearchParams(window.location.search)
        val source = params.get("source")

        if (source == "pwa") {
            pwaMain()
        } else {
            browserMain()
        }
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
