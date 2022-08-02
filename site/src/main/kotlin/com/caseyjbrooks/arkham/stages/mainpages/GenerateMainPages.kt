package com.caseyjbrooks.arkham.stages.mainpages

import com.caseyjbrooks.arkham.site.BuildConfig
import com.caseyjbrooks.arkham.stages.ProcessingStage
import com.caseyjbrooks.arkham.utils.processRecursively
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.extension
import kotlin.io.path.readText

class GenerateMainPages : ProcessingStage {
    override suspend fun process(repoRoot: Path, destination: Path) {
        processRecursively(
            repoRoot / "site/src/main/resources",
            destination,
            matcher = { it.extension == "html" },
        ) { path, os ->
            os.write(path.readText().replace("{{baseUrl}}", BuildConfig.BASE_URL).toByteArray())
        }
    }
}
