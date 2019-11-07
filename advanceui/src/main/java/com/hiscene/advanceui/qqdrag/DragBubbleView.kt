package com.hiscene.advanceui.qqdrag

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import com.hiscene.advanceui.BaseView
import com.hiscene.advanceui.R

/**
 * Created by junhu on 2019-11-07
 */
class DragBubbleView(context: Context?, attrs: AttributeSet?) : BaseView(context, attrs) {
    init {
        var typeArray = context?.obtainStyledAttributes(attrs, R.styleable.DragBubbleView)
        var color = typeArray?.getColor(R.styleable.DragBubbleView_bubble_color, Color.RED)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //1、静止状态：小球加消息数


        //2、拖拽连接状态：拖动1个小球加消息数，中间连接着贝塞尔曲线，起点一个大小可变的小球

        //3、分离状态：拖动1个小球加消息数

        //4、消失状态：小球爆炸效果
        paint.strokeWidth = 1f
        paint.getTextBounds("12",0,"12".length,Rect(0,0,100,100))
        canvas?.drawText("12",100f,100f,paint)

    }
}