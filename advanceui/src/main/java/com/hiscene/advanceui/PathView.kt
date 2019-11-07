package com.hiscene.advanceui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by junhu on 2019-11-07
 */
class PathView(context: Context?, attrs: AttributeSet?) : BaseView(context, attrs) {
    lateinit var path: Path

    var points:ArrayList<PointF>

    init {
        path = Path()

        points = arrayListOf()
        points.add(PointF(100f,100f))
        points.add(PointF(200f,0f))
        points.add(PointF(300f,100f))
        points.add(PointF(200f,300f))
        points.add(PointF(100f,200f))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)



        points.forEach {
            path.lineTo(it.x,it.y)
        }

//        //连线到绝对点
//        path.lineTo(200f,200f)
//
//        path.lineTo(250f,200f)
//
//        //连线到相对上一个终点移动多少偏移量
//        path.rLineTo(10f,30f)
//
//        //闭合曲线
//        path.close()

        //添加曲线
        //Add the specified arc to the path as a new contour.
        //float left, float top, float right, float bottom, float startAngle, float sweepAngle
//        path.addArc(200f,200f,400f,400f,-220f,220f)

        //Append the specified arc to the path as a new contour.
        //float left, float top, float right, float bottom, float startAngle,float sweepAngle, boolean forceMoveTo
//        path.arcTo()

//        path.addRect(50f,50f,200f,200f,Path.Direction.CW)
//
//        path.addCircle(50f,50f,100f,Path.Direction.CW)
//
//        path.addOval(10f,10f,100f,200f,Path.Direction.CW)


//        var path2 = Path()
//        path2.moveTo(100f,100f)
//        path2.lineTo(110f,100f)
//        //添加另一条path
//        path.addPath(path2)
//
//        //添加圆角矩形
//        var rect = RectF(200f,200f,300f,300f)
//        path.addRoundRect(rect,20f,20f,Path.Direction.CW)

        //移动到某个点
//        path.moveTo(300f,500f)
        //二阶贝塞尔曲线
//        path.quadTo(500f,100f,800f,500f)
//        //相对位置的二阶贝塞尔曲线
//        path.rQuadTo(200f,-400f,500f,0f)

        //三阶贝塞尔曲线cubicTo(float x1, float y1, float x2, float y2, float x3, float y3)

        canvas?.drawPath(path,paint)

    }
}