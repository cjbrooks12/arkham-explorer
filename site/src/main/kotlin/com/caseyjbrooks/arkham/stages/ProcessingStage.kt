package com.caseyjbrooks.arkham.stages

import java.nio.file.Path

interface ProcessingStage {

    suspend fun process(repoRoot: Path, destination: Path)
}
