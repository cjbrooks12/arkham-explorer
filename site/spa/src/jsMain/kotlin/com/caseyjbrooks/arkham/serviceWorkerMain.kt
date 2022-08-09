package com.caseyjbrooks.arkham

import com.caseyjbrooks.arkham.app.BuildConfig
import org.w3c.fetch.Response
import org.w3c.workers.ExtendableEvent
import org.w3c.workers.FetchEvent
import org.w3c.workers.InstallEvent
import org.w3c.workers.ServiceWorkerGlobalScope

external val self: ServiceWorkerGlobalScope

fun serviceWorkerMain() {
    val CACHE_NAME = "arkham-explorer-v1"

    self.oninstall = { event ->
        val installEvent = event as InstallEvent
        installEvent.waitUntil(
            self
                .caches
                .open(CACHE_NAME)
                .then { cache ->
                    return@then cache.addAll(
                        arrayOf(
                            "${BuildConfig.BASE_URL}/",
                            "${BuildConfig.BASE_URL}/index.html",
                            "${BuildConfig.BASE_URL}/spa.js",
                            "${BuildConfig.BASE_URL}/api/expansions.json",
                            "${BuildConfig.BASE_URL}/api/expansions/night-of-the-zealot.json",
                            "${BuildConfig.BASE_URL}/api/expansions/the-dunwich-legacy.json",
                        )
                    )
                }
        )
    }
    self.onactivate = { event ->
        val activateEvent = event as ExtendableEvent

        activateEvent
            .waitUntil(
                self
                    .caches
                    .open(CACHE_NAME)
                    .then { cache ->
                        cache
                            .keys()
                            .then { requests ->
                                requests.forEach { request ->
                                    cache.delete(request)
                                }
                            }
                    }
            )

        Unit
    }
    self.onfetch = onFetch@{ event ->
        val fetchEvent = event as FetchEvent
        val request = fetchEvent.request

        if (request.url.contains("chrome-extension://")) {
            // ignore the request
        } else {
            self
                .caches
                .open(CACHE_NAME)
                .then { cache ->
                    cache
                        .match(request)
                        .then { cachedResponse ->
                            (cachedResponse as Response?) ?: run {
                                self
                                    .fetch(request)
                                    .then { fetchedResponse ->
                                        cache.put(request, fetchedResponse)
                                        fetchedResponse
                                    }
                            }
                        }
                }
        }
    }
}
