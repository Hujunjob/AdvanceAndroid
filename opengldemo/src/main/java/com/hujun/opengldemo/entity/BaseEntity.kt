package com.hujun.myapplication.entity

import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.util.Log
import com.hujun.myapplication.utils.BufferHelper
import com.hujun.myapplication.utils.MathUtils
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL

/**
 * Created by junhu on 2019-11-29
 */
abstract class BaseEntity(program: Int) {
    val SIZE_OF_FLOAT = 4
    val SIZE_OF_INT = 4

    protected var VBO = 0

    protected var VAO = 0

    private var vbos = IntArray(1)

    protected var mProgram = program

    protected lateinit var mVertex: FloatArray

    protected var mWidth = 0
    protected var mHeight = 0

    init {
        GLES30.glUseProgram(mProgram)
        this.initVertex()
        this.generaVAO()
        this.generateVBO()
        this.handleVBO()
        Log.d(TAG, "init: program=$mProgram,VAO=$VAO,VBO=$VBO")
    }

    protected open fun afterInit() {
        GLES30.glBindVertexArray(0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    open fun sizeChanged(width: Int, height: Int) {
        mWidth = width
        mHeight = height
    }

    abstract fun draw()

    abstract fun initVertex()

    open fun release() {
        GLES30.glDeleteProgram(mProgram)
    }

    companion object {
        private val TAG = this::class.java.name.replace("${'$'}Companion", "").split(".").last()
    }

    fun checkGLError(msg: String = "") {
        var error = GLES30.glGetError()
        if (error != GLES30.GL_NO_ERROR) {
            Log.e(TAG, "$msg checkGLError: ${MathUtils.intToHex(error)}")
        }
    }

    protected open fun generateVBO(): Int {
        GLES30.glGenBuffers(1, vbos, 0)
        checkGLError("generateVBO")

        VBO = vbos[0]

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO)

        GLES30.glBufferData(
            GLES30.GL_ARRAY_BUFFER,
            mVertex.size * SIZE_OF_FLOAT,
            BufferHelper.generateBuffer(mVertex),
            GLES30.GL_STATIC_DRAW
        )
        return VBO
    }

    abstract fun handleVBO()

    protected open fun generaVAO() {
        var vaos = IntArray(1)

        GLES30.glGenVertexArrays(1, vaos, 0)

        VAO = vaos[0]

        GLES30.glBindVertexArray(VAO)
    }

    protected fun setUniform1i(uniformName:String,uniform:Int){
        val location = GLES30.glGetUniformLocation(mProgram,uniformName)
        GLES30.glUniform1i(location,uniform)
    }
}