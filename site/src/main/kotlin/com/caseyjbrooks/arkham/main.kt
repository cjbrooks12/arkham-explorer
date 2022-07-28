package com.caseyjbrooks.arkham

import com.copperleaf.json.pointer.JsonPointer
import com.copperleaf.json.pointer.JsonPointerAction
import com.copperleaf.json.pointer.mutate
import com.copperleaf.json.utils.toJsonString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import java.nio.file.Files
import java.util.Properties
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.div
import kotlin.io.path.name

fun main() = runBlocking {
    withContext(Dispatchers.IO) {
        val repoRoot = Path(".")
        val appResources = repoRoot / "app/src/jsMain/resources"
        val ahLcgSettingsInput = appResources / "ArkhamHorrorLCG/settings"
        val ahLcgSettingsOutput = appResources / "data/settings"

        Files.list(ahLcgSettingsInput).use { fileStream ->
            fileStream.forEach { path ->
                val inputAsProperties = Properties().apply { load(Files.newInputStream(path)) }

                var sourceObject: JsonElement = JsonObject(emptyMap())

                inputAsProperties.entries.forEach { (key, value) ->
                    val pointer = JsonPointer.parse("#/${key.toString().replace('-', '/')}")

                    try {
                        sourceObject = sourceObject.mutate(pointer, JsonPointerAction.SetValue(value))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                val outputFile = ahLcgSettingsOutput / path.name.replace(".settings", ".json")
                outputFile.deleteIfExists()
                outputFile.createFile()

                Files.write(outputFile, sourceObject.toJsonString(prettyPrint = true).toByteArray())
            }
        }
    }
}
