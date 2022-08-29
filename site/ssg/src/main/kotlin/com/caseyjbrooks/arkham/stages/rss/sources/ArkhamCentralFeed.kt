package com.caseyjbrooks.arkham.stages.rss.sources

import com.caseyjbrooks.arkham.dag.http.InputHttpNode
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.stages.rss.RssChannel
import com.caseyjbrooks.arkham.stages.rss.RssFeedSource
import io.ktor.client.HttpClient

// http://arkhamcentral.com/index.php/feed/
class ArkhamCentralFeed : RssFeedSource {
    public val tags = listOf("RssFeedAggregator", "input", "ArkhamCentral")

    override suspend fun loadFeed(
        http: HttpClient,
    ): InputHttpNode {
        return StartHttpNode(
            http,
            url = "http://arkhamcentral.com/index.php/feed/",
            tags = tags,
            responseFormat = "xml",
        )
    }

    override suspend fun parseFeed(node: InputHttpNode): RssChannel {
        TODO("Not yet implemented")
    }
}
