package com.placeworkers.imagedrawer

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.placeworkers.imageeditor.ImageConverterUtil
import com.placeworkers.imageeditor.ImageConverterUtil.toBase64
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resourcesBitmap = BitmapFactory.decodeResource(resources, R.drawable.mountains)
        val base64Image = resourcesBitmap.toBase64()

        val imageFromBase64 = ImageConverterUtil.imageFromBase64(base64Image)
        requireNotNull(imageFromBase64) {
            "Das Base64 darf nicht null sein!"
        }
        imageView.setImageBitmap(imageFromBase64)
        // FIXME: Du kannst nicht einfach hier so ein fetten Base64 String reinklatschen und in voller laenge anzeigen, das kann man sowieso nicht sinnvoll anzeigen.
        textView.text = base64Image
    }

}