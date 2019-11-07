package com.hiscene.advanceui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

/**
 * Created by junhu on 2019-11-07
 */
class PathView(context: Context?, attrs: AttributeSet?) : BaseView(context, attrs) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint
    }
}