package com.caseyjbrooks.arkham.stages.rss.sources

import com.caseyjbrooks.arkham.dag.http.InputHttpNode
import com.caseyjbrooks.arkham.dag.http.StartHttpNode
import com.caseyjbrooks.arkham.stages.rss.RssChannel
import com.caseyjbrooks.arkham.stages.rss.RssFeedSource
import io.ktor.client.HttpClient

// https://www.fantasyflightgames.com/en/rss/
class FantasyFlightNews : RssFeedSource {
    public val tags = listOf("RssFeedAggregator", "input", "Fantasy Flight News")

    override suspend fun loadFeed(
        http: HttpClient,
    ): InputHttpNode {
        return StartHttpNode(
            http,
            url = "https://www.fantasyflightgames.com/en/rss/",
            tags = tags,
            responseFormat = "xml",
        )
    }

    override suspend fun parseFeed(node: InputHttpNode): RssChannel {
        TODO("Not yet implemented")
    }
}
