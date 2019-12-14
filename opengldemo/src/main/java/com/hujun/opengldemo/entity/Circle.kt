package com.hujun.myapplication.entity

import android.opengl.GLES32
import android.util.Log

/**
 * Created by junhu on 2019-11-30
 */
class Circle(mProgram: Int) : BaseEntity(mProgram) {
//    private val STEP_ANGLE by lazy { 10 }

    companion object {
        private val TAG = this::class.java.name.replace("${'$'}Companion", "").split(".").last()
        //圆形扇形切分为多少一个
        val STEP_ANGLE = 10
        val RADIUS = 0.5f
    }

    init {
        setColor()
        afterInit()
    }

    override fun sizeChanged(width: Int, height: Int) {
        super.sizeChanged(width, height)
        GLES32.glUseProgram(mProgram)
        val aspect = mWidth.toDouble() / mHeight.toDouble()
        GLES32.glVertexAttrib1f(1, aspect.toFloat())
    }

    private fun setColor() {
        var color = GLES32.glGetUniformLocation(mProgram, "aColor")
        Log.e(TAG, "setColor: $color")
        checkGLError("setColor1")
        GLES32.glUniform4f(color, 0.5f, 0.1f, 0.0f, 1.0f)
        checkGLError("setColor2")
    }

    override fun draw() {
        checkGLError("before draw")
        if (mWidth == 0 || mHeight == 0) {
            Log.e(TAG, "draw: error width cannot be 0")
            return
        }
        GLES32.glUseProgram(mProgram)
        GLES32.glBindVertexArray(VAO)

        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, 0, mVertex.size / 3)

        GLES32.glBindVertexArray(0)
        GLES32.glUseProgram(0)
        checkGLError("after draw")
    }

    override fun initVertex() {
        //顶点包括1个圆心，扇形上切分点
        val size = 1 + 360 / STEP_ANGLE + 1
        mVertex = FloatArray(3 * size)
        //圆心
        mVertex[0] = 0f
        mVertex[1] = 0f
        mVertex[2] = 0f

        for (angle in 0..360 step STEP_ANGLE) {
            var num = (angle / STEP_ANGLE + 1) * 3
            var radian = Math.toRadians(angle.toDouble())
            mVertex[num] = Math.cos(radian).toFloat() * RADIUS
            mVertex[num + 1] = Math.sin(radian).toFloat() * RADIUS
            mVertex[num + 2] = 0f
        }
    }

    override fun handleVBO() {
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, VBO)
        GLES32.glVertexAttribPointer(0, 3, GLES32.GL_FLOAT, false, 3 * SIZE_OF_FLOAT, 0)
        GLES32.glEnableVertexAttribArray(0)
    }
}