package com.hujun.myapplication.entity

import android.opengl.GLES32
import com.hujun.myapplication.utils.BufferHelper

/**
 * Created by junhu on 2019-11-29
 */
class Triangle(mProgram: Int) : BaseEntity(mProgram) {
    init {
        afterInit()
    }
    override fun initVertex() {
        mVertex = floatArrayOf(
            //位置信息和颜色信息
            -0.5f, -0.5f, 0f, 1f, 0f, 0f,
            0.5f, -0.5f, 0f, 0f, 1f, 0f,
            0f, 0.5f, 0f, 0f, 0f, 1f
        )
    }

    override fun handleVBO() {
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, VBO)
        GLES32.glVertexAttribPointer(
            0,
            3,
            GLES32.GL_FLOAT,
            false,
            6 * SIZE_OF_FLOAT,
            0
        )
        GLES32.glEnableVertexAttribArray(0)

        GLES32.glVertexAttribPointer(
            1,
            3,
            GLES32.GL_FLOAT,
            false,
            6 * SIZE_OF_FLOAT,
            3 * SIZE_OF_FLOAT
        )
        GLES32.glEnableVertexAttribArray(1)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,0)
        checkGLError("handleVBO")
    }

    override fun draw() {
        checkGLError("draw before")
        GLES32.glUseProgram(mProgram)

        GLES32.glBindVertexArray(VAO)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, 3)
        GLES32.glBindVertexArray(0)

        checkGLError("draw after")
    }


}