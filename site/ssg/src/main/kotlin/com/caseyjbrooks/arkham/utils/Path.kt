package com.caseyjbrooks.arkham.utils

import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension

fun Path.withExtension(ext: String): Path {
    return this.resolveSibling("$nameWithoutExtension.$ext")
}

fun Path.asIndexHtml(): Path {
    return this.resolveSibling("$nameWithoutExtension/index.html")
}

fun Path.withSuffix(suffix: String): Path {
    return this.resolveSibling("$nameWithoutExtension$suffix.$extension")
}
