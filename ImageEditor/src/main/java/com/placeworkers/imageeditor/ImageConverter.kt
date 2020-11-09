package com.placeworkers.imageeditor

import java.io.ByteArrayOutputStream

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

// Manages the converting from an image to a string and vice versa
class ImageConverter {

    companion object {

        // creates a image object from a base64 encoded string
        fun imageFromBase64String(base64: String): Bitmap? {
            val base64ByteArray = base64StringToByteArray(base64)
            return byteArrayToImage(base64ByteArray)
        }

        // creates a base64 encoded string from an image
        fun base64StringFromImage(image: Bitmap): String? {
            val imageByteArray = imageToByteArray(image)
            return byteArrayToBase64String(imageByteArray)
        }

        // private helper functions
        // image to ByteArray and vice versa
        private fun imageToByteArray(bitMap: Bitmap): ByteArray? {
            val baos = ByteArrayOutputStream()
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            return baos.toByteArray()
        }

        private fun byteArrayToImage(byteArray: ByteArray): Bitmap? {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }

        // base64 string to ByteArray and vice versa
        private fun base64StringToByteArray(base64: String): ByteArray {
            return Base64.decode(base64, Base64.DEFAULT)
        }

        private fun byteArrayToBase64String(byteArray: ByteArray?): String? {
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

    }
}