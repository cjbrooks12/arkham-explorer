package com.caseyjbrooks.arkham

import com.caseyjbrooks.arkham.stages.copyscripts.CopyScripts
import com.caseyjbrooks.arkham.stages.expansiondata.FetchExpansionData
import com.caseyjbrooks.arkham.stages.mainpages.GenerateMainPages
import com.caseyjbrooks.arkham.stages.processimages.ProcessImages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.div
import kotlin.io.path.exists


fun main(): Unit = runBlocking {
    withContext(Dispatchers.IO) {
        val repoRoot = Path("./../")

        val destination = repoRoot / "build/dist"
        if (destination.exists()) {
            destination.toFile().deleteRecursively()
        }
        destination.createDirectories()

        val stages = listOf(
            ProcessImages(),
            FetchExpansionData(),
            CopyScripts(),
            GenerateMainPages(),
        )

        stages.forEach {
            it.process(repoRoot, destination)
        }
    }
}
