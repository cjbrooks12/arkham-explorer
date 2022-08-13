package com.caseyjbrooks.arkham

import com.caseyjbrooks.arkham.app.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.promise
import org.w3c.dom.events.Event
import org.w3c.fetch.Response
import org.w3c.workers.ExtendableEvent
import org.w3c.workers.FetchEvent
import org.w3c.workers.InstallEvent
import org.w3c.workers.ServiceWorkerGlobalScope
import kotlin.js.Promise

external val self: ServiceWorkerGlobalScope

fun onInstallEventFlow(): Flow<InstallEvent> {
    return callbackFlow<InstallEvent> {
        val cb = { event: Event ->
            val installEvent = event as InstallEvent
            println("emitting oninstall")
            trySend(installEvent)
        }

        self.oninstall = cb

        awaitClose { self.oninstall = null }
    }
}

fun onActivateEventFlow(): Flow<ExtendableEvent> {
    return callbackFlow<ExtendableEvent> {
        val cb = { event: Event ->
            val installEvent = event as ExtendableEvent
            println("emitting onactivate")
            trySend(installEvent)
        }

        self.onactivate = cb

        awaitClose { self.onactivate = null }
    }
}

fun onFetchEventFlow(): Flow<FetchEvent> {
    return callbackFlow<FetchEvent> {
        val cb = { event: Event ->
            val installEvent = event as FetchEvent
            trySend(installEvent)
        }

        self.onfetch = cb

        awaitClose { self.onfetch = null }
    }
        .filter {
            // ignore fetches from chrome extensions
            !it.request.url.contains("chrome-extension://")
        }
}

fun serviceWorkerMain() {
    val CACHE_NAME = "arkham-explorer-v1"
    val serviceWorkerCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    serviceWorkerCoroutineScope.launch {
        onInstallEventFlow()
            .onEach {
                println("handling oninstall")
                val cache = self.caches.open(CACHE_NAME).await()
                cache.addAll(
                    arrayOf(
                        "${BuildConfig.BASE_URL}/",
                        "${BuildConfig.BASE_URL}/index.html",
                        "${BuildConfig.BASE_URL}/spa.js",
                        "${BuildConfig.BASE_URL}/spa.js.map",
                        "${BuildConfig.BASE_URL}/api/expansions.json",
                    )
                ).await()
            }
            .launchIn(this)

        onActivateEventFlow()
            .onEach { activateEvent ->
                println("handling onactivate")
                coroutineScope {
                    activateEvent
                        .waitUntil(
                            promise {
                                val cache = self.caches.open(CACHE_NAME).await()
                                val keys = cache.keys().await()
                                keys.forEach { request ->
                                    cache.delete(request).await()
                                }
                            }
                        )
                }
            }
            .launchIn(this)

        onFetchEventFlow()
            .onEach {
                val request = it.request
                val cache = self.caches.open(CACHE_NAME).await()
                val cachedResponse = cache.match(request).await()

                val actualResponse: Response = if(cachedResponse is Response) {
                    cachedResponse
                } else {
                    self.fetch(request).await()
                }

                it.respondWith(
                    Promise.Companion.resolve(actualResponse)
                )
            }
            .launchIn(this)
    }
}
