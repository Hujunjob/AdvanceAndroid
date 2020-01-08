package com.hujun.myapplication.entity

import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES30
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
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        initEBO()
        afterInit()

        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
    }

    override fun draw() {
        checkGLError("before draw")
        GLES30.glUseProgram(mProgram)

        surfaceTexture.updateTexImage()

        GLES30.glBindVertexArray(VAO)

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0)

        GLES30.glBindVertexArray(0)
        GLES30.glUseProgram(0)
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
        GLES30.glGenTextures(1, textures, 0)
        var textureId = textures[0]

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        return textureId
    }

    override fun handleVBO() {
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO)
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 5 * SIZE_OF_FLOAT, 0)
        GLES30.glEnableVertexAttribArray(0)

        GLES30.glVertexAttribPointer(
            1,
            2,
            GLES30.GL_FLOAT,
            false,
            5 * SIZE_OF_FLOAT,
            3 * SIZE_OF_FLOAT
        )
        GLES30.glEnableVertexAttribArray(1)
    }

    private fun initEBO() {
        var ebos = IntArray(1)
        GLES30.glGenBuffers(1, ebos, 0)
        var ebo = ebos[0]

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GLES30.glBufferData(
            GLES30.GL_ELEMENT_ARRAY_BUFFER,
            indexArray.size * SIZE_OF_INT,
            BufferHelper.generateBuffer(indexArray),
            GLES30.GL_STATIC_DRAW
        )

    }
}