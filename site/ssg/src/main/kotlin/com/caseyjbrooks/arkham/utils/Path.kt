package com.caseyjbrooks.arkham.utils

import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.div
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension

fun Path.withExtension(ext: String): Path {
    return this.resolveSibling("$nameWithoutExtension.$ext")
}

fun Path.asIndexHtml(): Path {
    return this.resolveSibling("$nameWithoutExtension/index.html")
}

fun Path.withFilenameSuffix(suffix: String): Path {
    return this.resolveSibling("$nameWithoutExtension$suffix.$extension")
}

fun Path.withFilenamePrefix(suffix: String): Path {
    return this.resolveSibling("$suffix$nameWithoutExtension.$extension")
}

fun Path.withPrefix(suffix: String): Path {
    return Paths.get(suffix) / this
}
