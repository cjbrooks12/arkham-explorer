package com.caseyjbrooks.arkham.stages.rss

import com.caseyjbrooks.arkham.dag.http.InputHttpNode
import io.ktor.client.HttpClient

interface RssFeedSource {
    suspend fun loadFeed(http: HttpClient) : InputHttpNode

    suspend fun parseFeed(node: InputHttpNode): RssChannel
}

data class RssChannel(
    val title: String,
    val link: String,
    val description: String,
    val atomLink: String? = null,
    val language: String? = null,
    val copyright: String? = null,
    val lastBuildDate: String? = null,
    val items: List<RssFeedItem> = emptyList(),
)

data class RssFeedItem(
    val title: String,
    val link: String,
    val description: String,
    val guid: String,
)
