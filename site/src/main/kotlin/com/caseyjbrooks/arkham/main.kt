package com.caseyjbrooks.arkham

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
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

        copyRecursively(
            repoRoot / "app/build/distributions",
            destination
        )
        copyRecursively(
            repoRoot / "site/src/main/resources",
            destination
        )
    }
}

private suspend fun copyRecursively(src: Path, dest: Path) = withContext(Dispatchers.IO) {
    Files.walk(src).forEach { source ->
        val destination = dest.resolve(src.relativize(source))

        if(destination != dest) {
            Files.copy(source, dest.resolve(src.relativize(source)), StandardCopyOption.REPLACE_EXISTING)
        }
    }
}
