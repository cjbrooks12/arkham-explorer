package com.caseyjbrooks.arkham.dag.path

import com.caseyjbrooks.arkham.dag.Node
import java.io.OutputStream
import java.nio.file.Path

class TerminalPathNode(
    override val outputPath: Path,
    override val meta: Node.Meta,
    override val doRender: (List<Node>, OutputStream) -> Unit,
) : OutputPathNode {
    override var rendered: Boolean = false

    override fun toString(): String {
        return "TerminalPathNode(${meta.name})"
    }
}
