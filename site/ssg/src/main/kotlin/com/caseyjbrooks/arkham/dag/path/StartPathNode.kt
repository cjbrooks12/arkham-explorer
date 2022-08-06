package com.caseyjbrooks.arkham.dag.path

import com.caseyjbrooks.arkham.dag.Node
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.invariantSeparatorsPathString

data class StartPathNode(
    override val baseInputDir: Path,
    override val inputPath: Path,
    val tags: List<String> = emptyList(),
) : InputPathNode {
    override val meta: Node.Meta = Node.Meta(
        name = (baseInputDir / inputPath).invariantSeparatorsPathString,
        tags = tags,
    )

    override fun toString(): String {
        return "StartPathNode(${meta.name})"
    }
}
