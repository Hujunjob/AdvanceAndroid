package com.hujun.opengldemo.utils

import android.opengl.GLES20

/**
 * Created by junhu on 2019-11-26
 */
class TextureHelper {
    companion object{
        fun genTextures(textures:IntArray,target:Int = GLES20.GL_TEXTURE_2D){
            //首先生成纹理数组
            GLES20.glGenTextures(textures.size,textures,0)

            //C是面向过程的语言
            //绑定后的操作就是在该纹理上进行

            textures.forEach {
                //1.然后绑定纹理
                // C function void glBindTexture ( GLenum target, GLuint texture )
//                external fun glBindTexture(
//                    target: Int,  纹理目标
//                    texture: Int  纹理id
//                ): Unit
                GLES20.glBindTexture(target,it)

                //2.配置纹理
                //2.1 设置过滤参数
                // C function void glTexParameteri ( GLenum target, GLenum pname, GLint param )
//                external fun glTexParameteri(
//                    target: Int,  //纹理目标
//                    pname: Int,   //参数名
//                    param: Int    //参数值
//                ): Unit
                //放大过滤和缩小过滤，是当纹理被使用到一个比它大或下的形状上时，opengl该如何处理
                //GL_NEAREST是最近点采样，min通常用这个
                //GL_LINEAR是线性采样，mag通常用这个
                //参数 GL_TEXTURE_MAG_FILTER 缩小过滤
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR)
                //参数 GL_TEXTURE_MAG_FILTER 放大过滤
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST)

                //2.2. 设置纹理环绕
                //S方向就是x方向，T方向是y方向
                // 纹理坐标是0~1，如何超出范围的坐标，告诉OpenGL根据配置的参数进行处理
                //GL_REPEAT 重复拉伸（平铺），GL_CLAMP_TO_EDGE 截取拉伸（边缘拉伸）
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE)


                //操作完成后解绑
                //3.解绑纹理
                //传0表示与当前纹理解绑
                GLES20.glBindTexture(target,0)
            }
        }
    }
}