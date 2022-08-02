package com.caseyjbrooks.arkham.stages.processimages

import com.caseyjbrooks.arkham.stages.ProcessingStage
import com.caseyjbrooks.arkham.utils.ProcessUpdatedFile
import com.caseyjbrooks.arkham.utils.copyRecursively
import com.caseyjbrooks.arkham.utils.processCopiesRecursively
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.div
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension


class ProcessImages : ProcessingStage {
    override suspend fun process(repoRoot: Path, destination: Path) {
        // copy all assets normally
        copyRecursively(
            repoRoot / "app/build/distributions",
            destination,
        )

        // create a default PNG from each SVG
        processCopiesRecursively(
            repoRoot / "app/build/distributions",
            destination,
            matcher = { it.extension == "svg" },
            // copy with default size
            ProcessUpdatedFile(
                getUpdatedName = {
                    it.resolveSibling("${it.nameWithoutExtension}.png")
                },
                processFile = { path, os ->
                    val inputImage = ImageIO.read(path.toFile())
                    ImageIO.write(inputImage, "PNG", os)
                }
            ),
            // copy at 48, 64, 128, 256, 512, 1024 sizes for PNG
//            *listOf(48, 64, 128, 256, 512, 1024)
//                .flatMap { newHeight ->
//                    listOf("PNG" to "png").map { (format, extension) ->
//                        ProcessUpdatedFile(
//                            getUpdatedName = {
//                                it.resolveSibling("${it.nameWithoutExtension}_${newHeight}px.$extension")
//                            },
//                            processFile = { path, os ->
//                                val inputImage = ImageIO.read(path.toFile())
//
//                                val scaledImage = inputImage
//                                    .getScaledInstance(
//                                        (inputImage.width * (newHeight.toDouble() / inputImage.height)).roundToInt(),
//                                        newHeight,
//                                        Image.SCALE_SMOOTH
//                                    )
//                                    .toBufferedImage()
//
////                                val resampleOp = ResampleOp(
////                                    (inputImage.width * (newHeight.toDouble() / inputImage.height)).roundToInt(),
////                                    newHeight,
////                                )
////                                val scaledImage = resampleOp.filter(inputImage, null)
//                                ImageIO.write(scaledImage, format, os)
//                            }
//                        )
//                    }
//                }
//                .toTypedArray()
        )
    }
}
