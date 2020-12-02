package com.placeworkers.imageeditor.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

// Manages the converting from an image to a string and vice versa
object ImageConverterUtil {
    private const val IMAGE_QUALITY = 100

    // creates a image object from a base64 encoded string
    fun imageFromBase64(base64: String): Bitmap? {
        val base64ByteArray =
            base64StringToByteArray(
                base64
            )
        return base64ByteArray.toBitmap()
    }

    /**
     * Some cool extension function.
     */
    fun Bitmap.toBase64(): String {
        val outputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG,
            IMAGE_QUALITY, outputStream)
        return outputStream.toByteArray().toBase64()
    }

    fun Bitmap.resizedBitmap(newWidth: Int, newHeight: Int): Bitmap? {
        val width = this.width
        val height = this.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            this, 0, 0, width, height, matrix, false
        )
//        this.recycle()
        return resizedBitmap
    }

    private fun ByteArray.toBitmap(): Bitmap? = BitmapFactory.decodeByteArray(this, 0, this.size)

    // base64 string to ByteArray and vice versa
    private fun base64StringToByteArray(base64: String): ByteArray = Base64.decode(base64, Base64.DEFAULT)

    private fun ByteArray.toBase64() = Base64.encodeToString(this, Base64.DEFAULT)

    fun getBitmapFromPath(urlPath: String): Bitmap? {
        val file = File(urlPath)
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.absolutePath)

        } else return null
    }

    fun getBitmapFromAssets(context: Context, path: String): Bitmap? {
        val `is`: InputStream = context.assets.open(path)
        return BitmapFactory.decodeStream(`is`)
    }
}