package com.placeworkers.imageeditor

import java.io.ByteArrayOutputStream

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

// Manages the converting from an image to a string and vice versa
object ImageConverter {

    // creates a image object from a base64 encoded string
    fun imageFromBase64String(base64: String): Bitmap {
        val base64ByteArray = base64StringToByteArray(base64)
        return byteArrayToImage(base64ByteArray)
    }

    /**
     * Some cool extension function.
     */
    fun Bitmap.toBase64String() : String {
        val imageByteArray = imageToByteArray(this)
        return byteArrayToBase64String(imageByteArray)
    }

    // private helper functions
    // image to ByteArray and vice versa
    private fun imageToByteArray(bitMap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    private fun byteArrayToImage(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    // base64 string to ByteArray and vice versa
    private fun base64StringToByteArray(base64: String): ByteArray {
        return Base64.decode(base64, Base64.DEFAULT)
    }

    private fun byteArrayToBase64String(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}