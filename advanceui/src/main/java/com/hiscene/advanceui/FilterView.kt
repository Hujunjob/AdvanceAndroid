package com.hiscene.advanceui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View

/**
 * Created by junhu on 2019-11-06
 */
class FilterView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var paint: Paint
    var bitmap:Bitmap
//    var filter:LightingColorFilter
    var filter:ColorFilter

    init {
        paint = Paint()
        bitmap = BitmapFactory.decodeResource(resources,R.mipmap.lyf)
        /**
         * LightingColorFilter滤镜
         * LightingColorFilter(@ColorInt int mul, @ColorInt int add)
         * R' = R * mul.R / 0xff + add.R
         * G' = G * mul.G / 0xff + add.G
         * B' = B * mul.B / 0xff + add.B
         *
         * 实现效果：mul是相乘，比如0x00ffff，可以去除红色值
         * add是相加，可以提高某个色值，比如0x0000ff,可以提高蓝色值
         */
//        filter = LightingColorFilter(0xffffff,0x0000ff)

        /**
         * 让某种颜色跟图层进行混合，混合模式有很多种
         */
//        filter = PorterDuffColorFilter(Color.RED,PorterDuff.Mode.DARKEN)

        var colorMatrix = floatArrayOf(
            1f,0f,0f,0f,100f,            //red
            0f,1f,0f,0f,0f,            //green
            0f,0f,1f,0f,0f,            //blue
            0f,0f,0f,1f,0f             //alpha
        )

        filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = filter
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawBitmap(bitmap,0f,0f,paint)
    }
}