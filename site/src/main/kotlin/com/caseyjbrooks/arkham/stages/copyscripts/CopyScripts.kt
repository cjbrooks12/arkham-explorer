package com.caseyjbrooks.arkham.stages.copyscripts

import com.caseyjbrooks.arkham.stages.ProcessingStage
import com.caseyjbrooks.arkham.utils.processNonRecursively
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.name
import kotlin.io.path.readBytes

class CopyScripts : ProcessingStage {
    override suspend fun process(repoRoot: Path, destination: Path) {
        processNonRecursively(
            repoRoot / "app/build/distributions",
            destination,
            matcher = { it.name == "app.js" || it.name == "app.js.map" },
        ) { path, os ->
            os.write(path.readBytes())
        }
    }
}
