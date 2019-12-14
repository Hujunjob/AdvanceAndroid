package com.hujun.myapplication.utils

import android.content.Context
import android.opengl.GLES32
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
        var shaderId = GLES32.glCreateShader(type)

        GLES32.glShaderSource(shaderId, shader)

        GLES32.glCompileShader(shaderId)

        var status = IntArray(1)
        GLES32.glGetShaderiv(shaderId, GLES32.GL_COMPILE_STATUS, status, 0)
        if (status[0] != GLES32.GL_TRUE) {
            checkGLError("compileShader")
            var info = GLES32.glGetShaderInfoLog(shaderId)
            throw IllegalStateException("compileShader失败 $type info:$info")
        }

        return shaderId
    }

    private fun linkProgram(vShader: Int, fShader: Int): Int {
        var program = GLES32.glCreateProgram()

        GLES32.glAttachShader(program, vShader)
        GLES32.glAttachShader(program, fShader)

        GLES32.glLinkProgram(program)

        var status = IntArray(1)
        GLES32.glGetProgramiv(program, GLES32.GL_LINK_STATUS, status, 0)
        if (status[0] != GLES32.GL_TRUE) {
            var info = GLES32.glGetShaderInfoLog(program)
            throw IllegalStateException("linkProgram失败 : ${status[0]},info:$info")
        }
        GLES32.glDeleteShader(vShader)
        GLES32.glDeleteShader(fShader)
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
            var vShaderId = compileShader(vShader, GLES32.GL_VERTEX_SHADER)
            var fShaderId = compileShader(fShader, GLES32.GL_FRAGMENT_SHADER)
            mProgram = linkProgram(vShaderId, fShaderId)
            if (mProgram>0){
                Log.e(TAG, "linkProgram: 成功")
            }else{
                Log.e(TAG, "linkProgram: 失败 ")
            }
        }
    }
    fun checkGLError(msg: String = "") {
        var error = GLES32.glGetError()
        if (error != GLES32.GL_NO_ERROR) {
            Log.e(TAG, "$msg checkGLError: $error")
        }
    }

}