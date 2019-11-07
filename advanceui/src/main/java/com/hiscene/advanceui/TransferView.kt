package com.hiscene.advanceui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by junhu on 2019-11-07
 */
class TransferView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var paint: Paint

    init {
        paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 4f
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        println("ondraw")

//        paint.color =Color.RED
//        canvas?.drawRect(0f,0f,200f,200f,paint)
//        canvas?.translate(100f,100f)
//        paint.color = Color.BLACK
//        canvas?.drawRect(0f,0f,200f,200f,paint)

//        paint.color = Color.RED
//        canvas?.drawRect(0f, 0f, 200f, 200f, paint)
////        canvas?.scale(0.5f, 0.5f)
////        canvas?.scale(0.5f, 0.5f,200f,200f)
//        paint.color = Color.BLACK
//        canvas?.drawRect(0f, 0f, 200f, 200f, paint)
//
//        //旋转
//        paint.color = Color.RED
//        canvas?.drawRect(0f, 0f, 200f, 200f, paint)
//        //将旋转圆心放在(100,100)
//        canvas?.rotate(45f,100f,100f)
//        paint.color = Color.BLACK
//        canvas?.drawRect(0f, 0f, 200f, 200f, paint)

//        //倾斜
//        paint.color = Color.RED
//        canvas?.drawRect(0f, 0f, 200f, 200f, paint)
//        canvas?.skew(1f,0f)
//        paint.color = Color.BLACK
//        canvas?.drawRect(0f, 0f, 200f, 200f, paint)

//        paint.color = Color.RED
//        canvas?.drawRect(0f, 0f, 200f, 200f, paint)
//        //切割，画布被裁减，超出裁减区域就无法绘制了
//        canvas?.clipRect(0f, 0f, 200f, 200f)
//        paint.color = Color.BLACK
//        canvas?.drawRect(100f, 100f, 200f, 200f, paint)
//
//        //反向裁减，在这个区域内的就无法显示
//        canvas?.clipOutRect(0f, 0f, 200f, 200f)

        var matrix = Matrix()
        matrix.setTranslate(50f,50f)
        matrix.setScale(0.5f,0.5f)
        matrix.setRotate(45f)
        canvas?.matrix = matrix
        canvas?.drawRect(0f,0f,100f,100f,paint)

    }
}