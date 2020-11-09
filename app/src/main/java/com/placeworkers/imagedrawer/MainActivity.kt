package com.placeworkers.imagedrawer

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.placeworkers.imageeditor.ImageConverter
import com.placeworkers.imageeditor.ImageConverter.toBase64String
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resourcesBitmap = BitmapFactory.decodeResource(resources, R.drawable.mountains)
        val base64ImageString = resourcesBitmap.toBase64String()

        val imageFromBase64 = ImageConverter.imageFromBase64String(base64ImageString)
        image_view.setImageBitmap(imageFromBase64)
        // FIXME: Du kannst nicht einfach hier so ein fetten Base64 String reinklatschen in voller laenge
        text_view.text = base64ImageString
    }

}