package com.caseyjbrooks.arkham.stages.processimages

import com.caseyjbrooks.arkham.stages.ProcessingStage
import com.caseyjbrooks.arkham.utils.cache.CacheService
import com.caseyjbrooks.arkham.utils.cache.Cacheable
import com.caseyjbrooks.arkham.utils.rasterizeSvg
import com.caseyjbrooks.arkham.utils.withExtension
import com.caseyjbrooks.arkham.utils.withSuffix
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.io.path.extension

class ProcessImages(
    private val cacheService: CacheService
) : ProcessingStage {
    companion object {
        private const val VERSION = 1L
    }

    override suspend fun process(): Iterable<Cacheable.Input<*, *>> {
        return cacheService
            .getFilesInDirs("data")
            .filter { it.extension == "svg" }
            .map { inputPath ->
                val relativeInputPath = Paths.get("data").relativize(inputPath)

                Cacheable.Input.CachedPath(
                    processor = "ProcessImages",
                    version = ProcessImages.VERSION,
                    inputPath = inputPath,
                    rootDir = cacheService.rootDir,
                    outputs = {
                        buildList<Cacheable.Output.CachedPath> {
                            // copy over the SVG as-is
                            this += Cacheable.Output.CachedPath(
                                outputDir = cacheService.outputDir,
                                outputPath = relativeInputPath,
                            )

                            // Convert the SVG to its default PNG form, using whatever size was in the original SVG
                            this += Cacheable.Output.CachedPath(
                                outputDir = cacheService.outputDir,
                                outputPath = relativeInputPath.withExtension("png"),
                                render = { input, os ->
                                    os.use {
                                        val inputImage = ImageIO.read(input.realInput.toFile())
                                        ImageIO.write(inputImage, "PNG", os)
                                    }
                                }
                            )

                            // Resize the SVG into various sizes and render them each as PNGs
                            listOf(24, 48, 64, 128, 256, 512, 1024).forEach { newHeight ->
                                this += Cacheable.Output.CachedPath(
                                    outputDir = cacheService.outputDir,
                                    outputPath = relativeInputPath.withSuffix("_${newHeight}px").withExtension("png"),
                                    render = { input, os ->
                                        os.use {
                                            rasterizeSvg(input.realInput, newHeight, os)
                                        }
                                    }
                                )
                            }
                        }
                    }
                )
            }
    }
}
