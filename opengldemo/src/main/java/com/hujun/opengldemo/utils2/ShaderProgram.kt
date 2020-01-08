package com.hujun.myapplication.utils

import android.content.Context
import android.opengl.GLES30
import android.text.TextUtils
import android.util.Log
import java.lang.IllegalStateException

/**
 * Created by junhu on 2019-11-29
 */
class ShaderProgram(var context: Context, var vertexResourceId: Int, var fragmentResourceId: Int) {
    var mProgram = 0
        private set

    companion object{
        private val TAG = this::class.java.name.replace("${'$'}Companion","").split(".").last()

    }

    /**
     * 编译shader
     * @param shader shader的代码
     * @param type GL_VERTEX_SHADER或GL_FRAGMENT_SHADER
     */
    private fun compileShader(shader: String, type: Int): Int {
        var shaderId = GLES30.glCreateShader(type)

        GLES30.glShaderSource(shaderId, shader)

        GLES30.glCompileShader(shaderId)

        var status = IntArray(1)
        GLES30.glGetShaderiv(shaderId, GLES30.GL_COMPILE_STATUS, status, 0)
        if (status[0] != GLES30.GL_TRUE) {
            checkGLError("compileShader")
            var info = GLES30.glGetShaderInfoLog(shaderId)
            throw IllegalStateException("compileShader失败 $type info:$info")
        }

        return shaderId
    }

    private fun linkProgram(vShader: Int, fShader: Int): Int {
        var program = GLES30.glCreateProgram()

        GLES30.glAttachShader(program, vShader)
        GLES30.glAttachShader(program, fShader)

        GLES30.glLinkProgram(program)

        var status = IntArray(1)
        GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, status, 0)
        if (status[0] != GLES30.GL_TRUE) {
            var info = GLES30.glGetShaderInfoLog(program)
            throw IllegalStateException("linkProgram失败 : ${status[0]},info:$info")
        }
        GLES30.glDeleteShader(vShader)
        GLES30.glDeleteShader(fShader)
        return program
    }

    fun linkProgram() {
        var vShader =
            ShaderResourceLoader.loadResource(
                context,
                vertexResourceId
            )
        var fShader =
            ShaderResourceLoader.loadResource(
                context,
                fragmentResourceId
            )

        if (!TextUtils.isEmpty(vShader) && !TextUtils.isEmpty(fShader)) {
            var vShaderId = compileShader(vShader, GLES30.GL_VERTEX_SHADER)
            var fShaderId = compileShader(fShader, GLES30.GL_FRAGMENT_SHADER)
            mProgram = linkProgram(vShaderId, fShaderId)
            if (mProgram>0){
                Log.e(TAG, "linkProgram: 成功")
            }else{
                Log.e(TAG, "linkProgram: 失败 ")
            }
        }
    }
    fun checkGLError(msg: String = "") {
        var error = GLES30.glGetError()
        if (error != GLES30.GL_NO_ERROR) {
            Log.e(TAG, "$msg checkGLError: $error")
        }
    }

}