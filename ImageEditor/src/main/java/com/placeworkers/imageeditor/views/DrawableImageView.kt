package com.placeworkers.imageeditor.views


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.Toast
import com.placeworkers.imageeditor.utils.ImageConverterUtil
import com.placeworkers.imageeditor.utils.ImageConverterUtil.toBase64

@SuppressLint("AppCompatCustomView")
class DrawableImageView: ImageView, View.OnTouchListener {

    val LOG_TAG = "DrawableImageView"

    private lateinit var paint: Paint
    private lateinit var mCanvas: Canvas
    private lateinit var editImageMatrix: Matrix
    private lateinit var originalImage: Bitmap

    private lateinit var path: Path

    var downx = 0f
    var downy = 0f
    var upx = 0f
    var upy = 0f

    var mX = 0f
    var mY = 0f

    var bitmapPaint: Paint? = null
    var drawnBitmap: Bitmap? = null


    constructor(context: Context): super(context) { mInit() }
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) { mInit() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) { mInit() }

    init {
        Log.d("ZOOM_VIEW", "ZoomableImageView init!")
        // TODO: find out how to get the size of the parent
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun mInit() {
        setOnTouchListener(this)

        scaleType = ScaleType.CENTER_INSIDE

        // initialize the lateinit variables to prevent crashes
        path = Path()
        editImageMatrix = Matrix()
        originalImage = Bitmap.createBitmap(context.resources.displayMetrics.widthPixels,context.resources.displayMetrics.heightPixels, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(originalImage)
        paint = Paint()
    }

    fun setBase64Image(base64: String?) {
        if (base64 != null) {
            val givenBitmap = ImageConverterUtil.imageFromBase64(base64)
            val alteredBitmap = Bitmap.createBitmap(givenBitmap!!.width, givenBitmap!!.height, givenBitmap!!.config)
            setNewImage(alteredBitmap, givenBitmap!!)
        }
    }

    fun setImageURL(urlPath: String) {
        Log.d("DrawableImageView.kt", "setImageURL() URL: $urlPath")

        val bitmap = ImageConverterUtil.getBitmapFromPath(urlPath)
        if (bitmap!= null) {
            val alteredBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
            setNewImage(alteredBitmap, bitmap)
        } else {
            Toast.makeText(context, "Failed to load the file from path", Toast.LENGTH_LONG).show()
            mCanvas = Canvas(Bitmap.createBitmap(0,0, Bitmap.Config.ARGB_8888))
        }
    }

    fun setNewImage(alteredBitmap: Bitmap, bmp: Bitmap) {
        editImageMatrix = Matrix()
        drawnBitmap = alteredBitmap
        originalImage = bmp

        paint = Paint().apply {
            isAntiAlias = true
            isDither = true
            color = 0xFFFF0000.toInt()
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 20.0F
        }

        path = Path()
        mCanvas = Canvas(alteredBitmap)
        mCanvas.drawBitmap(bmp, editImageMatrix, paint)

        setImageBitmap(alteredBitmap)
    }

    fun clearCanvas(invalidate:Boolean = false) {
        drawnBitmap = Bitmap.createBitmap(originalImage.width, originalImage.height, originalImage.config)
//        mCanvas = Canvas(drawnBitmap!!)
//
//        path = Path()
        setNewImage(drawnBitmap!!, originalImage)

        if (invalidate) invalidate()
    }

    fun exportBitmap(): Bitmap {
        val combineBitmap = Bitmap.createBitmap(drawnBitmap!!.width, drawnBitmap!!.height, drawnBitmap!!.config)
        Log.d(LOG_TAG, "combine Bitmap: w: ${combineBitmap.width} h: ${combineBitmap.height}")

        val combineCanvas = Canvas(combineBitmap)
        combineCanvas.drawBitmap(originalImage, editImageMatrix, paint)
        combineCanvas.drawBitmap(drawnBitmap!!, 0F, 0F, paint)

        return combineBitmap
    }

    fun exportBitmapAsBase64(): String {
        val exportedBitmap = exportBitmap()
        return exportedBitmap.toBase64()
    }

    private fun touch_start(x: Float, y: Float) {
        path.reset()
        path.moveTo(x, y)
        mX = x
        mY = y
    }

    private val TOUCH_TOLERANCE = 4.0f

    private fun touch_move(x: Float, y: Float) {
        val dx: Float = Math.abs(x - mX)
        val dy: Float = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touch_up() {
        path.lineTo(mX, mY)
        // commit the path to our offscreen
        mCanvas.drawPath(path, paint)
        // kill this so we don't double draw
        path.reset()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        mCanvas.drawBitmap(drawnBitmap, 0, 0, mBitmapPaint);
        mCanvas.drawPath(path, paint)
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {

        val x = getPointerCoords(event)[0] //event.getX();
        val y = getPointerCoords(event)[1] //event.getY();

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
//                downx = getPointerCoords(event)[0] //event.getX();
//                downy = getPointerCoords(event)[1] //event.getY();
                touch_start(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
//                upx = getPointerCoords(event)[0] //event.getX();
//                upy = getPointerCoords(event)[1] //event.getY();
//                mCanvas.drawLine(downx, downy, upx, upy, paint)
                downx = upx
                downy = upy
                touch_move(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
//                upx = getPointerCoords(event)[0] //event.getX();
//                upy = getPointerCoords(event)[1] //event.getY();
//                mCanvas.drawLine(downx, downy, upx, upy, paint)
                touch_up()
                invalidate()
            }
        }

        return true
    }

    private fun getPointerCoords(e: MotionEvent): FloatArray {
        val index = e.actionIndex
        val coords = floatArrayOf(e.getX(index), e.getY(index))

        val matrix = Matrix()
        imageMatrix.invert(matrix)
        matrix.postTranslate(scrollX.toFloat(), scrollY.toFloat())
        matrix.mapPoints(coords)

        return coords
    }
}