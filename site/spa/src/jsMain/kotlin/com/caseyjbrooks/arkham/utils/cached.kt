package com.caseyjbrooks.arkham.utils

import androidx.compose.runtime.Composable
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.ballast.repository.cache.isLoading

@Composable
inline fun <T1 : Any> CacheReady(
    cached1: Cached<T1>,
    onAnyError: @Composable () -> Unit = {},
    onLoading: @Composable () -> Unit = {},
    content: @Composable (T1) -> Unit,
) {
    if (
        cached1 is Cached.FetchingFailed
    ) {
        onAnyError()
    } else if (
        cached1.isLoading()
    ) {
        onLoading()
    } else {
        val t1 = cached1.getCachedOrThrow()
        content(t1)
    }
}

@Composable
fun <T1 : Any, T2 : Any> CacheReady(
    cached1: Cached<T1>,
    cached2: Cached<T2>,
    onLoading: @Composable () -> Unit = {},
    onAnyError: @Composable (Throwable?, Throwable?) -> Unit = { _, _ ->},
    content: @Composable (T1, T2) -> Unit,
) {
    if (
        cached1 is Cached.FetchingFailed ||
        cached2 is Cached.FetchingFailed
    ) {
        onAnyError(
            (cached1 as? Cached.FetchingFailed<T1>)?.error,
            (cached2 as? Cached.FetchingFailed<T2>)?.error
        )
    } else if (
        cached1.isLoading() ||
        cached2.isLoading()
    ) {
        onLoading()
    } else {
        val t1 = cached1.getCachedOrThrow()
        val t2 = cached2.getCachedOrThrow()
        content(t1, t2)
    }
}

@Composable
fun <T1 : Any, T2 : Any, T3 : Any> CacheReady(
    cached1: Cached<T1>,
    cached2: Cached<T2>,
    cached3: Cached<T3>,
    onLoading: @Composable () -> Unit = {},
    onAnyError: @Composable () -> Unit = {},
    content: @Composable (T1, T2, T3) -> Unit,
) {
    if (
        cached1 is Cached.FetchingFailed ||
        cached2 is Cached.FetchingFailed ||
        cached3 is Cached.FetchingFailed
    ) {
        onAnyError()
    } else if (
        cached1.isLoading() ||
        cached2.isLoading() ||
        cached3.isLoading()
    ) {
        onLoading()
    } else {
        val t1 = cached1.getCachedOrThrow()
        val t2 = cached2.getCachedOrThrow()
        val t3 = cached3.getCachedOrThrow()
        content(t1, t2, t3)
    }
}
