package com.hiscene.advanceui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


/**
 * Created by junhu on 2019-11-06
 */

class XfermodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mPaint: Paint? = null
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    lateinit var rectBitmap: Bitmap
    lateinit var circleBitmap: Bitmap

    init {
        init()
    }


    private fun init() {
        //初始化画笔
        mPaint = Paint()
        mPaint!!.color = Color.RED
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        rectBitmap = createRectBitmap(mWidth, mHeight)
        circleBitmap = createCircleBitmap(mWidth, mHeight)
        //禁止硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        setBackgroundColor(Color.GRAY)
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //1.ComposeShader
        //2.画笔Paint.setXfermode()
        //3.PorterDuffColorFilter



        //        //离屏绘制
        var layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), mPaint, Canvas.ALL_SAVE_FLAG);

        //        //目标图
        canvas.drawBitmap(rectBitmap, 0f, 0f, mPaint)
        //        //设置混合模式
        mPaint!!.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        //        //源图，重叠区域右下角部分
        canvas.drawBitmap(circleBitmap, 0f, 0f, mPaint)
        //        //清除混合模式
        mPaint!!.setXfermode(null)
        //
        canvas.restoreToCount(layerId);

    }

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
