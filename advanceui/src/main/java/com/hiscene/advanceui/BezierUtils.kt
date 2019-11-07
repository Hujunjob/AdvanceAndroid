package com.hiscene.advanceui

import android.graphics.PointF

/**
 * Created by junhu on 2019-11-07
 */
class BezierUtils(private var points: List<PointF>) {
    fun setPoints(points: List<PointF>) {
        this.points = points
    }

    /**
     * 德卡斯特利奥算法，计算贝塞尔曲线
     * @param i 贝塞尔曲线阶数
     * @param j 控制点
     * @param t 时间
     * @return
     */
    fun deCasteljauX(i: Int, j: Int, t: Float): Float {
        if (i == 1) {
            return (1 - t) * points[j].x + t * points[j + 1].x
        }
        return (1 - t) * deCasteljauX(i - 1, j, t) + t * deCasteljauX(i - 1, j + 1, t)
    }

    /**
     * 德卡斯特利奥算法，计算贝塞尔曲线
     * @param i 贝塞尔曲线阶数
     * @param j 控制点
     * @param t 时间
     * @return
     */
    fun deCasteljauY(i: Int, j: Int, t: Float): Float {
        if (i == 1) {
            return (1 - t) * points[j].y + t * points[j + 1].y
        }
        return (1 - t) * deCasteljauY(i - 1, j, t) + t * deCasteljauY(i - 1, j + 1, t)
    }
}