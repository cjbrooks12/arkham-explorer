package com.caseyjbrooks.arkham.utils

import java.nio.file.Path
import kotlin.io.path.div

class SiteConfiguration(
    val rootDir: Path,
    val outputDir: Path,
    val hashesDir: Path,
    val httpCacheDir: Path,
) {
    constructor(
        rootDir: Path,
        outputPath: String,
        hashesPath: String,
        httpCachePath: String,
    ) : this(
        rootDir = rootDir,
        outputDir = rootDir / outputPath,
        hashesDir = rootDir / hashesPath,
        httpCacheDir = rootDir / httpCachePath,
    )
}
