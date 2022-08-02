package com.caseyjbrooks.arkham.utils

import java.awt.Dimension
import java.awt.Image
import java.awt.image.BufferedImage


fun getScaledDimension(imgSize: Dimension, boundary: Dimension): Dimension? {
    val original_width = imgSize.width
    val original_height = imgSize.height
    val bound_width = boundary.width
    val bound_height = boundary.height
    var new_width = original_width
    var new_height = original_height

    // first check if we need to scale width
    if (original_width > bound_width) {
        //scale width to fit
        new_width = bound_width
        //scale height to maintain aspect ratio
        new_height = new_width * original_height / original_width
    }

    // then check if we need to scale even with the new height
    if (new_height > bound_height) {
        //scale height to fit instead
        new_height = bound_height
        //scale width to maintain aspect ratio
        new_width = new_height * original_width / original_height
    }
    return Dimension(new_width, new_height)
}

fun Image.toBufferedImage(): BufferedImage? {
    val img = this
    if (img is BufferedImage) {
        return img
    }

    // Create a buffered image with transparency
    val bimage = BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB)

    // Draw the image on to the buffered image
    val bGr = bimage.createGraphics()
    bGr.drawImage(img, 0, 0, null)
    bGr.dispose()

    // Return the buffered image
    return bimage
}
