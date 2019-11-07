package com.hiscene.advanceui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by junhu on 2019-11-07
 */
class BezierView(context: Context?, attrs: AttributeSet?) : BaseView(context, attrs) {

    lateinit var points: ArrayList<PointF>

    lateinit var path: Path

    var bezierUtils: BezierUtils

    init {
        init()
        bezierUtils = BezierUtils(points)
    }

    fun init() {
        points = arrayListOf()
        var order = 5

        for (i in 1..order) {
            var p = PointF()
            p.x = (100f + Math.random() * 800f).toFloat()
            p.y = (100f + Math.random() * 800f).toFloat()
            points.add(p)
        }
        path = Path()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var i = 0
        paint.color = Color.GRAY
        for (p in points) {
            if (i == 0) {
                path.moveTo(p.x, p.y)
            } else {
                path.lineTo(p.x, p.y)
            }
            i++
            canvas?.drawCircle(p.x, p.y, 10f, paint)
        }
        canvas?.drawPath(path, paint)

        paint.color = Color.RED
        drawBezier()
        canvas?.drawPath(path, paint)
        path.reset()
    }

    private fun drawBezier() {
        path.reset()
        bezierUtils.setPoints(points)
        var t = 0f
        for (i in 0..1000) {
            if (i == 0) {
                path.moveTo(points[0].x, points[0].y)
            }

            t = (i * 0.001).toFloat()

            var x = bezierUtils.deCasteljauX(points.size - 1, 0, t)
            var y = bezierUtils.deCasteljauY(points.size - 1, 0, t)
            path.lineTo(x, y)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_DOWN) {
            init()
            invalidate()
        }
        return super.onTouchEvent(event)
    }
}