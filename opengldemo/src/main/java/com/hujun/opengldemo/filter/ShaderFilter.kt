package com.hiscene.agoraengine.opengl

import android.content.Context
import android.opengl.GLES30
import android.util.Log
import com.hujun.opengldemo.R
import com.hujun.opengldemo.utils.BufferHelper
import com.hujun.opengldemo.utils.ShaderHelper
import com.hujun.opengldemo.utils.TextResourceReader
import java.lang.IllegalStateException


/**
 * Created by junhu on 2019-11-28
 */
class ShaderFilter(context: Context) {
    private var VAO: Int = 0
    private var mProgram = 0

    companion object {
        private val TAG = this::class.java.name.replace("${'$'}Companion", "").split(".").last()
    }

    //位置和颜色全放在一个数组里
    private val vertexAndColor: FloatArray


    init {
        vertexAndColor = floatArrayOf( // 位置              // 颜色
                0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,  // 右下
                -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f,  // 左下
                0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f // 顶部
        )

        //初始化OpenGL程序Program
        initShader(context, R.raw.vtriangle, R.raw.ftriangle)
//        mProgram = ShaderHelper.linkProgram(vShaderPath, fShaderPath)
        VAO = generateVAO()
    }


    /**
     * 初始化OpenGL程序
     */
    private fun initShader(context: Context, vertexSourceId: Int, fragmentSourceId: Int) {
        //顶点着色器
        val vertext = TextResourceReader.read(context, vertexSourceId)
        //片元着色器
        val fragment = TextResourceReader.read(context, fragmentSourceId)

        Log.d(TAG, "initShader: vertext \n" + vertext)
        Log.d(TAG, "initShader: fragment \n" + fragment)

        mProgram = ShaderHelper.linkProgram(vertext, fragment)
        if (mProgram < 0) {
            throw IllegalStateException("初始化OpenGL失败")
        } else {
            Log.d(TAG, "initShader: linkProgram 成功")
        }
    }

    fun draw() {
        drawTriangle()
    }


    private fun drawTriangle() {
        GLES30.glUseProgram(mProgram)

        GLES30.glBindVertexArray(VAO)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)

        GLES30.glBindVertexArray(0)
    }


    private fun generateVBO(): Int {
        var vbos = IntArray(1)
        GLES30.glGenBuffers(1, vbos, 0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbos[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexAndColor.size * 4, BufferHelper.getFloatBuffer(vertexAndColor), GLES30.GL_STATIC_DRAW)
        return vbos[0]
    }

    private fun handleVBO() {
        //有两个属性需要赋值
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 6 * 4, 0)
        GLES30.glEnableVertexAttribArray(0)

        GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 6 * 4, 3 * 4)
        GLES30.glEnableVertexAttribArray(1)
    }

    private fun generateVAO(): Int {
        var vaos = IntArray(1)
        GLES30.glGenVertexArrays(1, vaos, 0)
        var vao = vaos[0]
        if (vao == 0) {
            Log.e(TAG, "generateVAO: error")
        } else {
            Log.e(TAG, "generateVAO: success")
        }
        GLES30.glBindVertexArray(vao)
        generateVBO()
        handleVBO()
        GLES30.glBindVertexArray(0)
        return vao
    }
}