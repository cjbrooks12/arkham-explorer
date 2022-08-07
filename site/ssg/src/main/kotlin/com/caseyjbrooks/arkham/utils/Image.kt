package com.caseyjbrooks.arkham.utils

import org.apache.batik.transcoder.SVGAbstractTranscoder
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.nio.file.Path
import javax.imageio.ImageIO

fun rasterizeSvg(input: Path, newHeight: Int, os: OutputStream, newWidth: Int? = null) {
    val inputImage = ImageIO.read(input.toFile())
    val originalWidth = inputImage.width
    val originalHeight = inputImage.height
    val actualNewWidth = newWidth ?: (originalWidth * (newHeight.toDouble() / originalHeight))

    val scaledImage = ByteArrayOutputStream()
        .apply {
            val transcoder = PNGTranscoder()
            transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, actualNewWidth.toFloat())
            transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, newHeight.toFloat())
            transcoder.transcode(TranscoderInput(input.toFile().inputStream()), TranscoderOutput(this))
            flush()
        }
        .toByteArray()
        .inputStream()
        .let { ImageIO.read(it) }

    ImageIO.write(scaledImage, "PNG", os)
}
