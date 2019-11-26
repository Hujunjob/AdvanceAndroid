package com.hujun.opengldemo.filter

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.util.Log
import com.hujun.opengldemo.R
import com.hujun.opengldemo.utils.BufferHelper
import com.hujun.opengldemo.utils.TextureHelper
import java.nio.Buffer

/**
 * Created by junhu on 2019-11-26
 * 负责将camera数据离屏渲染到FBO
 */
class CameraFilter(
    context: Context,
    vertexSourceId: Int = R.raw.camera_vertex,
    fragmentSourceId: Int = R.raw.camera_fragment
) :
    BaseFilter(context, vertexSourceId, fragmentSourceId) {
    private lateinit var mFramebufferTextures: IntArray
    var mtx: FloatArray? = null
    private val vMatrix: Int = 0
    private lateinit var mFramebuffers: IntArray

    companion object{
        private val TAG = this::class.java.name.replace("${'$'}Companion","").split(".").last()
    }

    override fun onReady(width: Int, height: Int) {
        super.onReady(width, height)
        //离屏渲染

        //1.创建FBO
        // C function void glGenFramebuffers ( GLsizei n, GLuint *framebuffers )
//        external fun glGenFramebuffers(
//            n: Int,  //FBO个数
//            framebuffers: IntArray?,  //保存FBO的id
//            offset: Int   //用数组中的第几个保存
//        ): Unit
        mFramebuffers = IntArray(1)
        GLES20.glGenFramebuffers(1, mFramebuffers, 0)

        //2、创建属于FBO的纹理
        mFramebufferTextures = IntArray(1)
        //创建并配置纹理
        TextureHelper.genTextures(mFramebufferTextures)

        //3、放FBO与纹理发生联系
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFramebufferTextures[0])

        //生成2D纹理图像
        // C function void glTexImage2D ( GLenum target, GLint level, GLint internalformat, GLsizei width, GLsizei height, GLint border, GLenum format, GLenum type, const GLvoid *pixels )
//        external fun glTexImage2D(
//            target: Int,
//            level: Int,
//            internalformat: Int,
//            width: Int,
//            height: Int,
//            border: Int,
//            format: Int,
//            type: Int,
//            pixels: Buffer?
//        ): Unit
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,
            0,
            GLES20.GL_RGBA,
            width,
            height,
            0,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            null
        )

        // C function void glFramebufferTexture2D ( GLenum target, GLenum attachment, GLenum textarget, GLuint texture, GLint level )
//        external fun glFramebufferTexture2D(
//            target: Int,
//            attachment: Int,
//            textarget: Int,
//            texture: Int,
//            level: Int
//        )
        //让fbo和纹理绑定起来
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebuffers[0])
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER,
            GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D,
            mFramebufferTextures[0],
            0
        )

        //配置完FBO纹理后，解绑
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
    }

    override fun initShaderIndex() {
        super.initShaderIndex()
        Log.d(TAG, "initShaderIndex: ")
        GLES20.glGetAttribLocation(mProgramId, "vMatrix")
    }

    override fun onDrawFrame(textureId: Int): Int {

        //1、设置视窗的宽高
        GLES20.glViewport(0, 0, mWidth, mHeight)

        //绑定FBO，因为是离屏渲染，需要渲染到FBO中
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebuffers[0])

        //2、使用着色器程序
        GLES20.glUseProgram(mProgramId)

        //3、渲染传值
        // 先传递顶点
        mVertexBuffer.clear()

        // C function void glVertexAttribPointer ( GLuint indx, GLint size, GLenum type, GLboolean normalized, GLsizei stride, GLint offset )
//        fun glVertexAttribPointer(
//            indx: Int,
//            size: Int,
//            type: Int,
//            normalized: Boolean,
//            stride: Int,
//            ptr: Buffer?
//        ): Unit
        //参数分别为：顶点坐标的索引，每个值的长度，值类型，是否归一化，步进（每次取完size后跳过多少个值取下一次值），数据
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer)

        //传递值后需要激活
        GLES20.glEnableVertexAttribArray(vPosition)

        //传递纹理
        mTextureBuffer.clear()
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer)
        GLES20.glEnableVertexAttribArray(vCoord)

        //4、变换矩阵
        if (mtx != null)
            GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0)

        //片元着色器
        //首先激活图层
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        //绑定纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        //然后再传递片元着色器参数
        GLES20.glUniform1f(vTexture, 0f)

        //通知OpenGL绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        //解绑纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
        return mFramebufferTextures[0]
    }


    /**
     * 初始化纹理坐标
     */
    override fun initTexture() {
        Log.d(TAG, "initTexture: ")
        //纹理的坐标系采用Android系统坐标系
        //屏幕边缘4个点
        //需要跟顶点坐标的顺序一一对应，且需要是OpenGL坐标系和Android屏幕坐标系对应
//        val TEXTURE = floatArrayOf(
//            0f, 0f,
//            0f, 1f,
//            1f, 0f,
//            1f, 1f
//        )

        //后摄顺时针旋转90度
//        val TEXTURE = floatArrayOf(
//            0f, 1f,
//            1f, 1f,
//            0f, 0f,
//            1f, 0f
//        )

        //前摄逆时针旋转90度
//        val TEXTURE = floatArrayOf(
//            1f, 0f,
//            0f, 0f,
//            1f, 1f,
//            0f, 1f
//        )
        //前摄逆时针旋转90度后再左右镜像
        val TEXTURE = floatArrayOf(
            1f, 1f,
            0f, 1f,
            1f, 0f,
            0f, 0f
        )
        mTextureBuffer = BufferHelper.getFloatBuffer(TEXTURE)
    }

}