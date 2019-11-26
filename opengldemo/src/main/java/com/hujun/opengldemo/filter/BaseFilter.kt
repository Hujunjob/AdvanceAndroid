package com.hujun.opengldemo.filter

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.hujun.opengldemo.utils.BufferHelper
import com.hujun.opengldemo.utils.ShaderHelper
import com.hujun.opengldemo.utils.TextResourceReader
import java.lang.IllegalStateException
import java.nio.FloatBuffer

/**
 * Created by junhu on 2019-11-26
 */
abstract class BaseFilter(context: Context, vertexSourceId: Int, fragmentSourceId: Int) {
    private var vTexture: Int = 0
    private var vCoord: Int = 0
    private var vPosition: Int = 0
    protected var mHeight: Int = 0
    protected var mWidth: Int = 0
    protected var mProgramId: Int = 0
    protected lateinit var mTextureBuffer: FloatBuffer
    protected lateinit var mVertexBuffer: FloatBuffer

    init {

        //初始化顶点坐标
        initVertex()

        //初始化纹理坐标
        initTexture()

        //初始化OpenGL程序Program
        initShader(context, vertexSourceId, fragmentSourceId)

        //获取shader里各个属性的索引值
        this.initShaderIndex()
    }

    fun release() {
        if (mProgramId > 0)
            GLES20.glDeleteProgram(mProgramId)
    }

    open fun onReady(width: Int, height: Int) {
        mWidth = width
        mHeight = height
    }

    protected fun onDrawFrame(textureId: Int): Int {

        //1、设置视窗的宽高
        GLES20.glViewport(0, 0, mWidth, mHeight)

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
//        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0)


        //片元着色器
        //首先激活图层
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        //绑定纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        //然后再传递片元着色器参数
        GLES20.glUniform1f(vTexture, 0f)

        //通知OpenGL绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        return textureId
    }

    /**
     * 获取shader里各个属性的索引值
     */
    private fun initShaderIndex() {
        //五、获取着色器程序变量的索引，通过索引来赋值
        //1. 顶点各个变量的索引
        vPosition = GLES20.glGetAttribLocation(mProgramId, "vPosition")
        vCoord = GLES20.glGetAttribLocation(mProgramId, "vCoord")

        //2、片元各个变量的索引
        vTexture = GLES20.glGetUniformLocation(mProgramId, "vTexture")
    }

    /**
     * 初始化OpenGL程序
     */
    private fun initShader(context: Context, vertexSourceId: Int, fragmentSourceId: Int) {
        //顶点着色器
        val vertext = TextResourceReader.read(context, vertexSourceId)
        //片元着色器
        val fragment = TextResourceReader.read(context, fragmentSourceId)

        mProgramId = ShaderHelper.linkProgram(vertext, fragment)
        if (mProgramId < 0) {
            throw IllegalStateException("初始化OpenGL失败")
        }
    }

    /**
     * 初始化纹理坐标
     */
    private fun initTexture() {
        //纹理的坐标系采用Android系统坐标系
        //屏幕边缘4个点
        //需要跟顶点坐标的顺序一一对应，且需要是OpenGL坐标系和Android屏幕坐标系对应
//        val t = floatArrayOf(
//            0f, 0f,
//            0f, 1f,
//            1f, 0f,
//            1f, 1f
//        )

        //后摄顺时针旋转90度
//        val t = floatArrayOf(
//            0f, 1f,
//            1f, 1f,
//            0f, 0f,
//            1f, 0f
//        )

        //前摄逆时针旋转90度
//        val t = floatArrayOf(
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

    /**
     * 初始化顶点坐标
     */
    private fun initVertex() {
        //采用OpenGL的坐标系
        //屏幕边缘4个点
        //不能采用顺时针或逆时针依次获取4个点，因为这样无法无法组成闭合的矩形
        // 例如顺时针四个点分别为 a b c d，则输入进去的点为a b d c
        val VERTEX = floatArrayOf(
            -1f, 1f,
            -1f, -1f,
            1f, 1f,
            1f, -1f
        )
        mVertexBuffer = BufferHelper.getFloatBuffer(VERTEX)

    }

}