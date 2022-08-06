package com.caseyjbrooks.arkham.dag.http

import com.caseyjbrooks.arkham.dag.Node
import io.ktor.client.HttpClient


data class StartHttpNode(
    override val httpClient: HttpClient,
    override val url: String,
    val tags: List<String> = emptyList(),
) : InputHttpNode {
    override val meta: Node.Meta = Node.Meta(
        name = url,
        tags = tags,
    )
    override fun toString(): String {
        return "StartHttpNode(${meta.name})"
    }
}
