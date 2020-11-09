package com.placeworkers.imagedrawer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.placeworkers.imageeditor.ImageConverter
import java.util.function.LongFunction
import kotlin.system.measureTimeMillis

const val MAIN_ACTIVITY = "MAIN_ACTIVITY"

class MainActivity : AppCompatActivity() {

    // preview for the image / bitmap
    private lateinit var imageView: ImageView

     // preview for the base64 String
    private lateinit var textView: TextView

    private var originalBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.image_view)
        textView = findViewById(R.id.text_view)

        // load the BitMap from the resources
        val decodeTime = measureTimeMillis {
            originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.mountains)
        }
        Log.d(MAIN_ACTIVITY, "Decoding Image from Resources has taken $decodeTime milliseconds (${decodeTime / 1000} seconds)")
        Log.d(MAIN_ACTIVITY, "Finished decoding from Resources")

        // measure the time of image to string
        val imageToBase64Time = measureTimeMillis {
            originalBitmap?.let {
                ImageConverter.base64StringFromImage(it).let { base64 ->
                    textView.text = base64
                }
            }
        }
        Log.d(MAIN_ACTIVITY, "Converting Image To base64 with Size of Height: ${originalBitmap?.height} Width: ${originalBitmap?.width} takes: $imageToBase64Time milliseconds (${imageToBase64Time / 1000} seconds)")
        Log.d(MAIN_ACTIVITY, "Finished converting to string")

        // measure the time of string to image
        val base64ToImageTime = measureTimeMillis {
            textView.text?.let { text ->
                ImageConverter.imageFromBase64String(text as String)?.let { convertedBitmap ->
                    imageView.setImageBitmap(convertedBitmap)
                }
            }
        }
        Log.d(MAIN_ACTIVITY, "Converting from base64 to Image with a size of Height: ${originalBitmap?.height} Width: ${originalBitmap?.width} takes: $base64ToImageTime milliseconds (${base64ToImageTime / 1000} seconds)")
        Log.d(MAIN_ACTIVITY, "Finished converting to image")

    }

    override fun onStart() {
        super.onStart()
    }

}