package com.hiscene.advanceui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by junhu on 2019-11-06
 */
class DView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var paint: Paint

    private var mWidth: Int = 400
    private var mHeight: Int = 400

    lateinit var rectBitmap: Bitmap
    lateinit var circleBitmap: Bitmap

    init {
        paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.FILL_AND_STROKE
//        setBackgroundColor(Color.GRAY)
        rectBitmap = createRectBitmap(mWidth, mHeight)
        circleBitmap = createCircleBitmap(mWidth, mHeight)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //禁止硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        setBackgroundColor(Color.GRAY)
        //离屏绘制
//        var layerId = canvas?.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), paint,Canvas.ALL_SAVE_FLAG)

        canvas?.drawBitmap(rectBitmap, 100f, 100f, paint)
        paint.setXfermode ( PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        canvas?.drawBitmap(circleBitmap, 10f, 10f, paint)
        paint.setXfermode (null)
//        canvas?.restoreToCount(layerId!!)
    }

//    private fun createRectBitmap(width: Int, height: Int): Bitmap {
//        var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//        var canvas = Canvas(bitmap)
//        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
//        paint.color = 0xff66aaff.toInt()
//        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
//        return bitmap
//    }
//
//    private fun createCircleBitmap(width: Int, height: Int): Bitmap {
//        var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//        var canvas = Canvas(bitmap)
//        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
//        paint.color = 0xffffcc44.toInt()
//        canvas.drawCircle(width * 0.5f, height * 0.5f, height * 0.5f, paint)
//        return bitmap
//    }


    //画矩形Dst
    fun createRectBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val dstPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dstPaint.setColor(-0x995501)
        canvas.drawRect(Rect(width / 20, height / 3, 2 * width / 3, 19 * height / 20), dstPaint)
        return bitmap
    }

    //画圆src
    fun createCircleBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val scrPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        scrPaint.setColor(-0x33bc)
        canvas.drawCircle(width * 2 / 3f, height / 3f, height / 4f, scrPaint)
        return bitmap
    }

}