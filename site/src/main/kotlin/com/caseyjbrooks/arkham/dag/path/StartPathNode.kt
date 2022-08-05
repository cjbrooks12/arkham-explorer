package com.caseyjbrooks.arkham.dag.path

import com.caseyjbrooks.arkham.dag.Node
import java.nio.file.Path

data class StartPathNode(
    override val inputPath: Path,
    override val meta: Node.Meta,
) : InputPathNode {
    override fun toString(): String {
        return "StartPathNode(${meta.name})"
    }
}
