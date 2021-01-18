package com.placeworkers.imagedrawer

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.placeworkers.imageeditor.utils.ImageConverterUtil
import com.placeworkers.imageeditor.utils.ImageConverterUtil.toBase64
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.system.measureTimeMillis

const val MAIN_ACTIVITY = "MAIN_ACTIVITY"

class MainActivity : AppCompatActivity() {

    private lateinit var resourcesBitmap: Bitmap
    private lateinit var base64Image: String

    private lateinit var alteredBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get the mountain image from the app modules "assets" resources
        // saved in assets to get no distractions from the scaling which comes from the density of a drawable folder (hdpi, xhdpi, xxhdpi etc.)
        resourcesBitmap = ImageConverterUtil.getBitmapFromAssets(baseContext, "mountains.jpg")!!

        // get a base64 string representation of the bitmap
        // .toBase64() function is a extension function and comes with the ImageEditor lib
        base64Image = resourcesBitmap.toBase64()

        // create a copy of the image which can be edited
        alteredBitmap = Bitmap.createBitmap(resourcesBitmap.width, resourcesBitmap.height, resourcesBitmap.config)

        // set a new image on the imageView to draw on
        // imageView variable comes with android magic. its defined in the /res/layout/activiy_main.xml file
        //  <com.placeworkers.imageeditor.views.DrawableImageView  android:id="@+id/imageView">
        imageView.setNewImage(alteredBitmap, resourcesBitmap)
    }

    private fun drawCircle(bm: Bitmap): Bitmap {
        val bmOverlay = Bitmap.createBitmap(bm.width, bm.height, bm.config)
        val canvas = Canvas(bmOverlay)
        val paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 1.0f
        canvas.drawBitmap(bm, Matrix(), null)
        canvas.drawCircle(50.0f, 50.0f, 25.0f, paint)
        return bmOverlay
    }

    // create a menu in the top navigation bar to save and clear images
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_layout, menu)
        return true
    }

    // handle selection of menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }

        R.id.action_save -> {
            Log.d(MAIN_ACTIVITY, "onOptionsItemSelected(action: Save)")
            // User chose the "Save" action, create a base64 representation of the image and pass it to a subscriber
            val convertingTime = measureTimeMillis {
                val exportImage = imageView.exportBitmap()
                base64Image = exportImage.toBase64()
//                imageView.setBase64Image(base64Image)
                imageView.setImageBitmap(exportImage)

                val imageName = "${UUID.randomUUID()}.png"
                MediaStore.Images.Media.insertImage(contentResolver, exportImage, imageName, "drawing")

//                val exportImage = imageView.exportBitmap()
//                alteredBitmap = Bitmap.createBitmap(exportImage.width, exportImage.height, exportImage.config)
//                imageView.setImageBitmap(exportImage)
            }

            Toast.makeText(this as Context, "Finished saving Bitmap to Base64. Took: $convertingTime ms", Toast.LENGTH_LONG).show()

            true
        }

        R.id.action_clear -> {
            imageView.clearCanvas(true)

            Toast.makeText(this, "clearing canvas", Toast.LENGTH_SHORT).show()

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}