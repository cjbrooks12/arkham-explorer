package com.caseyjbrooks.arkham.stages.processimages

import com.caseyjbrooks.arkham.stages.ProcessingStage
import com.caseyjbrooks.arkham.utils.ProcessUpdatedFile
import com.caseyjbrooks.arkham.utils.copyRecursively
import com.caseyjbrooks.arkham.utils.processCopiesRecursively
import org.apache.batik.transcoder.SVGAbstractTranscoder
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.ImageTranscoder
import org.apache.batik.transcoder.image.PNGTranscoder
import java.io.ByteArrayOutputStream
import java.io.OutputStream
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
            *listOf(24, 48, 64, 128, 256, 512, 1024)
                .flatMap { newHeight ->
                    listOf(ImageConverter("PNG", "png", PNGTranscoder())).map { format ->
                        ProcessUpdatedFile(
                            getUpdatedName = {
                                it.resolveSibling("${it.nameWithoutExtension}_${newHeight}px.${format.outputFileExtension}")
                            },
                            processFile = { path, os ->
                                rasterizeSvg(path, newHeight, format, os)
                            }
                        )
                    }
                }
                .toTypedArray()
        )
    }

    data class ImageConverter(
        val imageIoFormat: String,
        val outputFileExtension: String,
        val transcoder: ImageTranscoder,
    )

    private fun rasterizeSvg(input: Path, newHeight: Int, converter: ImageConverter, os: OutputStream) {
        val inputImage = ImageIO.read(input.toFile())
        val originalWidth = inputImage.width
        val originalHeight = inputImage.height
        val newWidth = (originalWidth * (newHeight.toDouble() / originalHeight))

        val scaledImage = ByteArrayOutputStream()
            .apply {
                converter.transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, newWidth.toFloat())
                converter.transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, newHeight.toFloat())
                converter.transcoder.transcode(TranscoderInput(input.toFile().inputStream()), TranscoderOutput(this))
                flush()
            }
            .toByteArray()
            .inputStream()
            .let { ImageIO.read(it) }

        ImageIO.write(scaledImage, converter.imageIoFormat, os)
    }
}
