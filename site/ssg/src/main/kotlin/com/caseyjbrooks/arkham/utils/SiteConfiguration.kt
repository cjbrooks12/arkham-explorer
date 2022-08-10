package com.caseyjbrooks.arkham.utils

import java.nio.file.Path
import kotlin.io.path.div

class SiteConfiguration(
    val rootDir: Path,

    val assetsDir: Path = rootDir / "content/assets",
    val expansionsDir: Path = rootDir / "content/expansions",
    val staticDir: Path = rootDir / "content/static",
    val pagesDir: Path = rootDir / "content/pages",

    val outputDir: Path = rootDir / "build/dist",
    val hashesDir: Path = rootDir / "build/cache/site",
    val httpCacheDir: Path = rootDir / "build/cache/http",
)
