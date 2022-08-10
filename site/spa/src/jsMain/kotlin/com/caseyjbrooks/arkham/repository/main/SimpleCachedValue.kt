package com.caseyjbrooks.arkham.repository.main

import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.map
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

public class SimpleCachedValue<State : Any>(
    val key: Key<State>,
    val doFetch: suspend () -> Any
) {
    data class Key<State : Any>(
        val type: String,
        val id: String?,
    )

    private val innerFlow = MutableStateFlow<Cached<State>>(Cached.NotLoaded())

    public suspend fun fetchWithCache(
        forceRefresh: Boolean,
    ): Unit {
        // VM is not ready to start fetching yet
        val currentState: Cached<State> = innerFlow.value

        // VM is already fetching when another request came in. If the second request is a forced refresh, cancel the first
        // by restarting the side-job. If the second reqeust is not a forced refresh, return and allow the original to
        // continue executing.
        if (currentState is Cached.Fetching<*> && !forceRefresh) return

        val initialValue = innerFlow.value
        val currentValueUnboxed = initialValue.getCachedOrNull()
        val currentValue = if (forceRefresh) {
            // if forcing a refresh, first mark it as not loaded (but keep the previous value for a better UI experience
            // when re-fetching)
            Cached.NotLoaded(currentValueUnboxed).also { innerFlow.value = it }
        } else {
            // otherwise, use the existing value as the current cached value
            initialValue
        }

        // if we have not loaded yet, have requested a forced refresh, or the previous attempt to fetch failed, try fetching
        // from the remote source now
        if (currentValue is Cached.NotLoaded<State> || currentValue is Cached.FetchingFailed<State>) {
            innerFlow.value = Cached.Fetching(currentValueUnboxed)

            val result = try {
                coroutineScope { Cached.Value(doFetch() as State) }
            } catch (t: Throwable) {
                Cached.FetchingFailed(t, currentValueUnboxed)
            }

            innerFlow.value = result
        }
    }

    fun <T : Any> asFlow(): Flow<Cached<T>> = innerFlow
        .map { it.map { it as T } }
}
