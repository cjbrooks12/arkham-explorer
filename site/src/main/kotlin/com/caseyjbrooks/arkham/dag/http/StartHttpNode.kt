package com.caseyjbrooks.arkham.dag.http

import com.caseyjbrooks.arkham.dag.Node
import io.ktor.client.HttpClient


data class StartHttpNode(
    override val meta: Node.Meta,
    override val httpClient: HttpClient,
    override val url: String,
) : InputHttpNode {
    override fun toString(): String {
        return "StartHttpNode(${meta.name})"
    }
}
