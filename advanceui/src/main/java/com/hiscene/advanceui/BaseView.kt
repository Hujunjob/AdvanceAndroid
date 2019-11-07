package com.hiscene.advanceui

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by junhu on 2019-11-07
 */
open class BaseView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var paint: Paint

    var mWidth = 0
    var mHeight = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
//        paint.strokeWidth = mHeight * 0.5f
    }

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
    }
}