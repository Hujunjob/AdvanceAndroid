package com.hujun.opengldemo.utils

import android.opengl.GLES30
import android.util.Log

/**
 * Created by junhu on 2019-11-26
 */
class ShaderHelper {
    companion object {
        private val TAG = this::class.java.name.replace("${'$'}Companion", "").split(".").last()

        /**
         * 编译着色器
         * @param type : 着色器类型，GLES30.GL_VERTEX_SHADER和GLES30.GL_FRAGMENT_SHADER
         * @param shader : 着色器代码
         * @return 着色器id
         */
        private fun compileShader(type: Int, shader: String): Int {

            //一、配置顶点着色器
            //1、创建着色器
            val vShaderId = GLES30.glCreateShader(type)

            Log.d(TAG, "compileShader: " + shader)

            //2、绑定代码到着色器上面，把着色器里的代码加载的着色器里
            GLES30.glShaderSource(vShaderId, shader)

            //3、编译着色器代码
            GLES30.glCompileShader(vShaderId)

            val status = IntArray(1)
            //4、主动获取编译是否成功状态
            GLES30.glGetShaderiv(vShaderId, GLES30.GL_COMPILE_STATUS, status, 0)

            if (status[0] != GLES30.GL_TRUE) {
//                throw IllegalStateException("配置顶点着色器失败")
                var info = GLES30.glGetShaderInfoLog(vShaderId)
                Log.e(TAG, "compileShader: 配置着色器失败 type=$type,code= ${status[0]}, info : $info")

                return -1
            }
            return vShaderId
        }

        /**
         * 编译顶点着色器
         * @param shader 顶点着色器代码
         * @return 顶点着色器id，返回小于0代表失败
         */
        fun compileVertexShader(shader: String): Int {
            return compileShader(GLES30.GL_VERTEX_SHADER, shader)
        }

        /**
         * 编译片元着色器
         * @param shader 片元着色器代码
         * @return 片元着色器id，返回小于0代表失败
         */
        fun compileTextureShader(shader: String): Int {
            return compileShader(GLES30.GL_FRAGMENT_SHADER, shader)
        }


        /**
         * @param fShaderId 顶点着色器id
         * @param vShaderId 片元着色器id
         */
        fun linkProgram(vShaderId: Int, fShaderId: Int): Int {
            //三、着色器程序
            //1. 创建新的OpenGL程序
            var mProgramId = GLES30.glCreateProgram()

            //2. 将着色器附加到程序，两个着色器都附加
            GLES30.glAttachShader(mProgramId, vShaderId)
            GLES30.glAttachShader(mProgramId, fShaderId)

            //3. 链接程序
            GLES30.glLinkProgram(mProgramId)

            val status = IntArray(1)
            //获得状态
            GLES30.glGetProgramiv(mProgramId, GLES30.GL_LINK_STATUS, status, 0)
            if (status[0] != GLES30.GL_TRUE) {
                Log.e(
                        TAG,
                        "linkProgram: 绑定program失败",
                        IllegalStateException("link program:" + GLES30.glGetProgramInfoLog(mProgramId))
                )
                return -1
            }

            //四、释放、删除着色器
            //链接完成后，着色器都放到了OpenGL程序Program里，则着色器可以删除了
            GLES30.glDeleteShader(vShaderId)
            GLES30.glDeleteShader(fShaderId)

            return mProgramId
        }

        fun linkProgram(vertexShader: String, textureShader: String): Int {
            val vertexShaderId = compileVertexShader(vertexShader)
            if (vertexShaderId < 0) {
                return -1
            } else {
                Log.d(TAG, "linkProgram: 顶点着色器编译成功")
            }

            val textureShaderId = compileTextureShader(textureShader)
            if (textureShaderId < 0) {
                GLES30.glDeleteShader(vertexShaderId)
                return -1
            } else {
                Log.d(TAG, "linkProgram: 片元着色器编译成功")
            }

            val programId = linkProgram(vertexShaderId, textureShaderId)
            if (programId > 0) {
                Log.d(TAG, "linkProgram:链接成功")
            }
            //四、释放、删除着色器
            //链接完成后，着色器都放到了OpenGL程序Program里，则着色器可以删除了
            GLES30.glDeleteShader(vertexShaderId)
            GLES30.glDeleteShader(textureShaderId)
            return programId
        }

        /**
         * 验证程序
         */
        fun validateProgram(programId: Int): Boolean {
            GLES30.glValidateProgram(programId)
            val status = IntArray(1)
            GLES30.glGetProgramiv(programId, GLES30.GL_VALIDATE_STATUS, status, 0)
            Log.e(
                    TAG,
                    "validateProgram: ${status[0]}, 程序日志：${GLES30.glGetProgramInfoLog(programId)}"
            )
            return status[0] != 0
        }
    }
}