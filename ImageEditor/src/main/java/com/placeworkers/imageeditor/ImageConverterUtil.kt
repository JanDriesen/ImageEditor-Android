package com.placeworkers.imageeditor

import java.io.ByteArrayOutputStream

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

// Manages the converting from an image to a string and vice versa
object ImageConverterUtil {
    private const val IMAGE_QUALITY = 100

    // creates a image object from a base64 encoded string
    fun imageFromBase64(base64: String): Bitmap? {
        val base64ByteArray = base64StringToByteArray(base64)
        return base64ByteArray.toBitmap()
    }

    /**
     * Some cool extension function.
     */
    fun Bitmap.toBase64(): String {
        val outputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream)
        return outputStream.toByteArray().toBase64()
    }

    private fun ByteArray.toBitmap(): Bitmap? = BitmapFactory.decodeByteArray(this, 0, this.size)

    // base64 string to ByteArray and vice versa
    private fun base64StringToByteArray(base64: String): ByteArray = Base64.decode(base64, Base64.DEFAULT)

    private fun ByteArray.toBase64() = Base64.encodeToString(this, Base64.DEFAULT)

}