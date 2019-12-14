package com.hujun.myapplication.entity

import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES32
import com.hujun.myapplication.utils.BufferHelper

/**
 * Created by junhu on 2019-11-30
 */
class CameraRender(program: Int) : BaseEntity(program) {
    var textureId = 0
        private set

    var surfaceTexture: SurfaceTexture
        private set

    companion object {
        private val indexArray = intArrayOf(
            0, 1, 3,
            1, 2, 3
        )
    }

    init {
        textureId = initTexture()
        surfaceTexture = SurfaceTexture(textureId)
        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        initEBO()
        afterInit()

        GLES32.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
    }

    override fun draw() {
        checkGLError("before draw")
        GLES32.glUseProgram(mProgram)

        surfaceTexture.updateTexImage()

        GLES32.glBindVertexArray(VAO)

        GLES32.glDrawElements(GLES32.GL_TRIANGLES, 6, GLES32.GL_UNSIGNED_INT, 0)

        GLES32.glBindVertexArray(0)
        GLES32.glUseProgram(0)
        checkGLError("after draw")
    }

    override fun initVertex() {
//        mVertex = floatArrayOf(
//            //顶点位置信息 + 纹理信息
//            -1f, -1f, 0f, 0f, 0f,
//            1f, -1f, 0f, 1f, 0f,
//            1f, 1f, 0f, 1f, 1f,
//            -1f, 1f, 0f, 0f, 1f
//        )

        mVertex = floatArrayOf(
            //顶点位置信息 + 纹理信息
            -1f, -1f, 0f, 1f, 1f,
            1f, -1f, 0f, 1f, 0f,
            1f, 1f, 0f, 0f, 0f,
            -1f, 1f, 0f, 0f, 1f
        )
    }

    private fun initTexture(): Int {
        var textures = IntArray(1)
        GLES32.glGenTextures(1, textures, 0)
        var textureId = textures[0]

        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        return textureId
    }

    override fun handleVBO() {
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, VBO)
        GLES32.glVertexAttribPointer(0, 3, GLES32.GL_FLOAT, false, 5 * SIZE_OF_FLOAT, 0)
        GLES32.glEnableVertexAttribArray(0)

        GLES32.glVertexAttribPointer(
            1,
            2,
            GLES32.GL_FLOAT,
            false,
            5 * SIZE_OF_FLOAT,
            3 * SIZE_OF_FLOAT
        )
        GLES32.glEnableVertexAttribArray(1)
    }

    private fun initEBO() {
        var ebos = IntArray(1)
        GLES32.glGenBuffers(1, ebos, 0)
        var ebo = ebos[0]

        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GLES32.glBufferData(
            GLES32.GL_ELEMENT_ARRAY_BUFFER,
            indexArray.size * SIZE_OF_INT,
            BufferHelper.generateBuffer(indexArray),
            GLES32.GL_STATIC_DRAW
        )

    }
}