package com.caseyjbrooks.arkham.dag.http

//
//data class HttpNode(
//    override val meta: Node.Meta,
//    val httpClient: HttpClient,
//    val url: String,
//) : Node {
//
//    init {
//        check(meta.nodeType == Node.NodeType.Output) {
//            "HttpNodes can only be Inputs"
//        }
//    }
//
//    suspend inline fun <reified T> fetch(graph: DependencyGraph): T {
//        return httpClient.get(url).body()
//    }
//
//    override fun dirty(graph: DependencyGraph): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun prepareOutput(graph: DependencyGraph) {
//        TODO("Not yet implemented")
//    }
//
//    override fun renderOutput(graph: DependencyGraph) {
//        TODO("Not yet implemented")
//    }
//
//    override fun markClean(graph: DependencyGraph) {
//        TODO("Not yet implemented")
//    }
//
//    override fun toString(): String {
//        return "HttpNode(${meta.name})"
//    }
//
//}
