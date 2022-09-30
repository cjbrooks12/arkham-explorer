@file:Suppress("OPT_IN_USAGE")

package com.caseyjbrooks.arkham

import com.caseyjbrooks.arkham.app.BuildConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.promise
import org.w3c.dom.events.Event
import org.w3c.fetch.Response
import org.w3c.workers.ExtendableEvent
import org.w3c.workers.FetchEvent
import org.w3c.workers.InstallEvent
import org.w3c.workers.ServiceWorkerGlobalScope

external val self: ServiceWorkerGlobalScope

val CACHE_NAME = "arkham-explorer-v1"

fun doOnInstall(doWork: suspend (InstallEvent) -> Unit) {
    self.oninstall = { event: Event ->
        val installEvent = event as InstallEvent
        installEvent.waitUntil(
            GlobalScope.promise {
                doWork(installEvent)
            }
        )
    }
}

fun doOnActivate(doWork: suspend (ExtendableEvent) -> Unit) {
    self.onactivate = { event: Event ->
        val activateEvent = event as ExtendableEvent
        activateEvent.waitUntil(
            GlobalScope.promise {
                doWork(activateEvent)
            }
        )
    }
}

fun doOnFetch(doWork: suspend (FetchEvent) -> Response) {
    self.onfetch = { event: Event ->
        val fetchEvent = event as FetchEvent
        if (fetchEvent.request.url.contains("chrome-extension://")) {
            // ignore the event, letting it be executed as normal
        } else {
            // otherwise, handle the fetch with this service worker to cache it
            fetchEvent.respondWith(
                GlobalScope.promise<Response> {
                    doWork(fetchEvent)
                }
            )
        }
    }
}

fun serviceWorkerMain() {
    doOnInstall {
        val cache = self.caches.open(CACHE_NAME).await()
        cache.addAll(
            arrayOf(
                "${BuildConfig.BASE_URL}/",
                "${BuildConfig.BASE_URL}/index.html",
                "${BuildConfig.BASE_URL}/bulmaswatch-lux.min.css",
                "${BuildConfig.BASE_URL}/bulmaswatch-lux.min.css.map",
                "${BuildConfig.BASE_URL}/spa.js",
                "${BuildConfig.BASE_URL}/api/expansions.json",
            )
        ).await()
    }
    doOnActivate {
        val cache = self.caches.open(CACHE_NAME).await()
        val keys = cache.keys().await()
        keys.forEach { request ->
            cache.delete(request).await()
        }

        self.clients.claim().await()
    }
    doOnFetch {
        val request = it.request
        val cache = self.caches.open(CACHE_NAME).await()
        val cachedResponse = cache.match(request).await()

        val actualResponse: Response = if (cachedResponse is Response) {
            cachedResponse
        } else {
            val newFetchedValue: Response = self.fetch(request).await()

            if (newFetchedValue.ok) {
                cache.put(request, newFetchedValue.clone()).await()
            }

            newFetchedValue
        }

        actualResponse
    }
}
