package com.caseyjbrooks.arkham.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile

/**
 * Copies files from [src] to [dest] without making any modifications to those files. This will recursively process each
 * subdirectory within the [src]
 */
suspend fun copyRecursively(src: Path, dest: Path) = withContext(Dispatchers.IO) {
    Files.walk(src).forEach { source ->
        val destination = dest.resolve(src.relativize(source))

        if (destination != dest) {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
        }
    }
}

/**
 * Copies files from [src] to [dest], processing each non-directory File with [processFile]. This is a 1-to-1 mapping
 * of each input file to an output file.
 *
 * [matcher] is used to determine if the File should be processed. Directories will always be created, regular Files
 * will only be processed if this lambda returns true. The Path given to the lambda is the File in the src directory.
 *
 * [getUpdatedName] receives the Path corresponding to the source File in the [dest] directory. You may return another
 * Path to change the output File name.
 *
 * [processFile] does the actual work of processing a File (for example, compiling Markdown). The source Path is the
 * File in the [src] directory, and the processing should write directly to the [OutputStream] given to it.
 */
suspend fun processRecursively(
    src: Path,
    dest: Path,
    matcher: (Path) -> Boolean,
    getUpdatedName: (Path) -> Path = { it },
    processFile: (Path, OutputStream) -> Unit,
) = withContext(Dispatchers.IO) {
    Files
        .walk(src)
        .forEach { source -> processPath(src, source, dest, matcher, ProcessUpdatedFile(getUpdatedName, processFile)) }
}


/**
 * Copies files from [src] to [dest], processing each non-directory File with [processFile]. This is a 1-to-1 mapping
 * of each input file to an output file.
 *
 * [matcher] is used to determine if the File should be processed. Directories will always be created, regular Files
 * will only be processed if this lambda returns true. The Path given to the lambda is the File in the src directory.
 *
 * [getUpdatedName] receives the Path corresponding to the source File in the [dest] directory. You may return another
 * Path to change the output File name.
 *
 * [processFile] does the actual work of processing a File (for example, compiling Markdown). The source Path is the
 * File in the [src] directory, and the processing should write directly to the [OutputStream] given to it.
 */
suspend fun processCopiesRecursively(
    src: Path,
    dest: Path,
    matcher: (Path) -> Boolean,
    vararg processors: ProcessUpdatedFile
) = withContext(Dispatchers.IO) {
    Files
        .walk(src)
        .forEach { source -> processPath(src, source, dest, matcher, *processors) }
}

suspend fun processNonRecursively(
    src: Path,
    dest: Path,
    matcher: (Path) -> Boolean,
    getUpdatedName: (Path) -> Path = { it },
    processFile: (Path, OutputStream) -> Unit,
) = withContext(Dispatchers.IO) {
    Files.list(src)
        .forEach { source -> processPath(src, source, dest, matcher, ProcessUpdatedFile(getUpdatedName, processFile)) }
}

private fun processPath(
    srcDir: Path,
    sourceFile: Path,
    destinationDir: Path,
    matcher: (Path) -> Boolean,
    vararg processors: ProcessUpdatedFile,
) {
    val destinationFile = destinationDir.resolve(srcDir.relativize(sourceFile))

    if (destinationFile != destinationDir) {
        if (sourceFile.isRegularFile()) {
            if (matcher(sourceFile)) {
                // process the file and write it to the output
                processors.forEach { it.process(sourceFile, destinationFile) }
            }
        } else {
            // copy it normally
            if (!destinationFile.exists()) {
                Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }
}

class ProcessUpdatedFile(
    val getUpdatedName: (Path) -> Path = { it },
    val processFile: (Path, OutputStream) -> Unit,
) {
    fun process(
        sourceFile: Path,
        destinationFile: Path,
    ) {
        // process the file and write it to the output
        val updatedDestination = getUpdatedName(destinationFile)
        Files.newOutputStream(updatedDestination).use {
            processFile(sourceFile, it)
        }
    }
}
