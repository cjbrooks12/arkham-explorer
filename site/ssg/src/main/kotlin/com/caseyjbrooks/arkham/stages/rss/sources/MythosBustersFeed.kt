package com.caseyjbrooks.arkham.stages.rss.sources

import com.caseyjbrooks.arkham.dag.http.InputHttpNode
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.stages.rss.RssChannel
import com.caseyjbrooks.arkham.stages.rss.RssFeedSource
import io.ktor.client.HttpClient

// https://mythosbusters.com/feed/
class MythosBustersFeed : RssFeedSource {
    public val tags = listOf("RssFeedAggregator", "input", "MythosBusters")

    override suspend fun loadFeed(
        http: HttpClient,
    ): InputHttpNode {
        return StartHttpNode(
            http,
            url = "https://mythosbusters.com/feed/",
            tags = tags,
            responseFormat = "xml",
        )
    }

    override suspend fun parseFeed(node: InputHttpNode): RssChannel {
        TODO("Not yet implemented")
    }
}
