package com.hiscene.advanceui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by junhu on 2019-11-07
 */
class RestoreView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var paint: Paint

    init {
        paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 4f
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, 200f, 200f, paint)
        //在平移前，保存当前状态
        canvas?.save()

        canvas?.translate(100f, 100f)

        paint.color = Color.BLACK
        canvas?.drawRect(0f, 0f, 200f, 200f, paint)
        //恢复平移前的状态
        canvas?.restore()

        canvas?.drawRect(50f, 50f, 200f, 200f, paint)


        var state = canvas?.save()

        canvas?.restoreToCount(state!!)

        var layId = canvas?.saveLayer(0f,0f,100f,100f,paint)
        //将这两个中间绘制的东西保存下来，然后在restoreToCount时一次性绘制到canvas上
        canvas?.drawRect(0f,0f,100f,100f,paint)
        //平移操作只在这个区域有用，在restoreToCount后又恢复
        canvas?.translate(100f,100f)

        canvas?.restoreToCount(layId!!)
    }
}