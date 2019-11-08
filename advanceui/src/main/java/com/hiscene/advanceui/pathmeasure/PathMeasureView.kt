package com.hiscene.advanceui.pathmeasure

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.hiscene.advanceui.BaseView
import com.hiscene.advanceui.R

/**
 * Created by junhu on 2019-11-08
 */
class PathMeasureView(context: Context?, attrs: AttributeSet?) : BaseView(context, attrs) {
    private var linePaint: Paint
    private var matrixx: Matrix
    private var bitmap: Bitmap

    init {
        linePaint = Paint()
        linePaint.color = Color.RED
        linePaint.strokeWidth = 5f
        linePaint.style = Paint.Style.STROKE

        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE

        matrixx = Matrix()

        var options = BitmapFactory.Options()
        options.inSampleSize = 8
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.lyf, options)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        canvas?.drawLine(0f, height * 0.5f, width.toFloat(), height * 0.5f, linePaint)
//        canvas?.drawLine(width * 0.5f, 0f, width * 0.5f, height.toFloat(), linePaint)

        canvas?.translate(width * 0.5f, height * 0.5f)

//        var path = Path()
//        path.lineTo(0f, 200f)
//        path.lineTo(200f, 200f)
//        path.lineTo(200f, 0f)
//
//        canvas?.drawPath(path, paint)
//
//        var pathMeasure = PathMeasure()
//        //测量path长度，第二个参数forceClosed代表是否强制path闭合的长度
//        pathMeasure.setPath(path, true)
//
//        println("path length=${pathMeasure.length}")
//
//        var pm2 = PathMeasure(path, false)
//        println("path length = ${pm2.length}")
//
//        //如果path发生了变化，PathMeasure需要重新设定path
//        path.lineTo(200f,-200f)
//        pm2.setPath(path,false)


//        var path = Path()
//        path.addRect(-200f,-200f,200f,200f,Path.Direction.CW)
//
//        var pathDst = Path()
//
//        var pathMeasure = PathMeasure(path,false)
//        //截取path的一段到dstpath中
//        pathMeasure.getSegment(200f,1000f,pathDst,true)
//
//        canvas?.drawPath(path,paint)
//        canvas?.drawPath(pathDst,linePaint)

//        var path = Path()
//        path.addRect(-100f,-100f,100f,100f,Path.Direction.CW)
//        path.addOval(-200f,-200f,200f,200f,Path.Direction.CW)
//        canvas?.drawPath(path,paint)
//
//        var pathMeasure = PathMeasure()
//        pathMeasure.setPath(path,false)
//        //计算的只是前一个矩形的长度
//        //pathmeasure计算的只是当前path的长度，而不是整个path的长度
//        println("path length=${pathMeasure.length}")
//        //计算下一个圆形的长度
//        pathMeasure.nextContour()
//        println("path length=${pathMeasure.length}")

//        var path = Path()
//        path.addCircle(0f, 0f, 200f, Path.Direction.CW)
//        canvas?.drawPath(path, paint)
//
//        var pos = FloatArray(2)
//        var tan = FloatArray(2)
//        var pathMeasure = PathMeasure(path, false)
//        var distance = 0f
//        //返回pos和tan，
//        // distance代表在与起始点距离为distance的点的坐标
//        // pos是这个点的坐标
//        // tan是这个点的斜率，可以算出斜率直线和x轴的夹角
//        pathMeasure.getPosTan(distance, pos, tan)
//        for (p in pos)
//            println("pos = $p")
//        for (t in tan)
//            println("tan = $t")
//
//        canvas?.drawCircle(pos[0], pos[1], 10f, paint)
//        //计算切线和x轴的夹角
//        var angle = Math.toDegrees(Math.atan2(tan[1].toDouble(), tan[0].toDouble()))
//        println("angle=$angle")
//
//
//        distance = pathMeasure.length / 8f
//        pathMeasure.getPosTan(distance, pos, tan)
//        for (p in pos)
//            println("pos = $p")
//        for (t in tan)
//            println("tan = $t")
//
//        paint.color = Color.RED
//        canvas?.drawCircle(pos[0], pos[1], 10f, paint)
//        angle = Math.toDegrees(Math.atan2(tan[1].toDouble(), tan[0].toDouble()))
//        println("angle=$angle")


        var path = Path()
        path.addCircle(0f, 0f, 200f, Path.Direction.CW)
        canvas?.drawPath(path, paint)
        var pathMeasure = PathMeasure(path, false)
        //将pos和tan信息都保存在matrix中
        pathMeasure.getMatrix(
            0f,
            matrixx,
            PathMeasure.POSITION_MATRIX_FLAG or PathMeasure.TANGENT_MATRIX_FLAG
        )
        //将图片旋转中心放到图片中心位置
        matrixx.preTranslate(-bitmap.width/2f,-bitmap.height/2f)
        canvas?.drawBitmap(bitmap, matrixx, paint)


//
//        var pos = FloatArray(2)
//        var tan = FloatArray(2)
//        var pathMeasure = PathMeasure(path, false)
//        var distance = 0f
//        //返回pos和tan，
//        // distance代表在与起始点距离为distance的点的坐标
//        // pos是这个点的坐标
//        // tan是这个点的斜率，可以算出斜率直线和x轴的夹角
//        pathMeasure.getPosTan(distance, pos, tan)
//        //计算切线和x轴的夹角
//        var angle = Math.toDegrees(Math.atan2(tan[1].toDouble(), tan[0].toDouble()))
//        matrixx.reset()
//        //进行角度旋转
//        matrixx.postRotate(angle.toFloat())
//        //进行位移
//        matrixx.postTranslate(pos[0] - bitmap.width / 2, pos[1] - bitmap.height / 2)
//        canvas?.drawBitmap(bitmap,matrixx,paint)


    }

}