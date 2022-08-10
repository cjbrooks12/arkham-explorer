package com.caseyjbrooks.arkham.dag.path

import com.caseyjbrooks.arkham.dag.Node
import java.io.OutputStream
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.invariantSeparatorsPathString

data class TerminalPathNode(
    override val baseOutputDir: Path,
    override val outputPath: Path,
    override val doRender: suspend (List<Node>, OutputStream) -> Unit,
    val tags: List<String> = emptyList(),
) : OutputPathNode {
    override val meta: Node.Meta = Node.Meta(
        name = (baseOutputDir / outputPath).invariantSeparatorsPathString,
        tags = tags,
    )

    override var rendered: Boolean = false

    override fun toString(): String {
        return "TerminalPathNode(${meta.name})"
    }
}
