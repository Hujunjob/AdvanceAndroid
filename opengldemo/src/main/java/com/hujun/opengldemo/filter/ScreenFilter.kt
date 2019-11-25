package com.hujun.opengldemo.filter

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.hujun.opengldemo.R
import com.hujun.opengldemo.utils.TextResourceReader
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Created by junhu on 2019-11-25
 */
class ScreenFilter(var mContext: Context) {

    private var mVertexBuffer: FloatBuffer
    private var mTextureBuffer: FloatBuffer
    private var vTexture: Int
    private var vMatrix: Int
    private var vCoord: Int
    private var vPosition: Int
    private var mWidth = 0
    private var mHeight = 0
    var mProgramId = 0

    fun onReady(width: Int, height: Int) {
        mWidth = width
        mHeight = height
    }

    fun onDrawFrame(textureId: Int, mtx: FloatArray) {
        //1、设置视窗的宽高
        GLES20.glViewport(0,0,mWidth,mHeight)

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
        GLES20.glVertexAttribPointer(vPosition,2,GLES20.GL_FLOAT,false,0,mVertexBuffer)

        //传递值后需要激活
        GLES20.glEnableVertexAttribArray(vPosition)


        //传递纹理
        mTextureBuffer.clear()
        GLES20.glVertexAttribPointer(vCoord,2,GLES20.GL_FLOAT,false,0,mTextureBuffer)
        GLES20.glEnableVertexAttribArray(vCoord)

        //4、变换矩阵
        GLES20.glUniformMatrix4fv(vMatrix,1,false,mtx,0)


        //片元着色器
        //首先激活图层
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        //绑定纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,textureId)
        //然后再传递片元着色器参数
        GLES20.glUniform1f(vTexture,0f)

        //通知OpenGL绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4)
    }


    init {
        //顶点着色器
        val vertext = TextResourceReader.read(mContext, R.raw.camera_vertex)
        //片元着色器
        val fragment = TextResourceReader.read(mContext, R.raw.camera_fragment)

        //一、配置顶点着色器
        //1、创建着色器
        val vShaderId = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)

        //2、绑定代码到着色器上面，把着色器里的代码加载的着色器里
        GLES20.glShaderSource(vShaderId, vertext)

        //3、编译着色器代码
        GLES20.glCompileShader(vShaderId)

        var status = IntArray(1)
        //4、主动获取编译是否成功状态
        GLES20.glGetShaderiv(vShaderId, GLES20.GL_COMPILE_STATUS, status, 0)

        if (status[0] != GLES20.GL_TRUE) {
            throw IllegalStateException("配置顶点着色器失败")
        }


        //一、配置片元着色器
        //1、创建着色器
        val fShaderId = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)

        //2、绑定代码到着色器上面，把着色器里的代码加载的着色器里
        GLES20.glShaderSource(fShaderId, fragment)

        //3、编译着色器代码
        GLES20.glCompileShader(fShaderId)

        //4、主动获取编译是否成功状态
        GLES20.glGetShaderiv(fShaderId, GLES20.GL_COMPILE_STATUS, status, 0)

        if (status[0] != GLES20.GL_TRUE) {
            throw IllegalStateException("配置片元着色器失败")
        }


        //三、着色器程序
        //1. 创建新的OpenGL程序
        mProgramId = GLES20.glCreateProgram()

        //2. 将着色器附加到程序，两个着色器都附加
        GLES20.glAttachShader(mProgramId, vShaderId)
        GLES20.glAttachShader(mProgramId, fShaderId)

        //3. 链接程序
        GLES20.glLinkProgram(mProgramId)

        //4. 获取链接状态
        GLES20.glGetShaderiv(fShaderId, GLES20.GL_LINK_STATUS, status, 0)
        if (status[0] != GLES20.GL_TRUE) {
            throw IllegalStateException("链接片元着色器失败")
        }
        GLES20.glGetShaderiv(vShaderId, GLES20.GL_LINK_STATUS, status, 0)
        if (status[0] != GLES20.GL_TRUE) {
            throw IllegalStateException("链接顶点着色器失败")
        }

        //四、释放、删除着色器
        //链接完成后，着色器都放到了OpenGL程序Program里，则着色器可以删除了
        GLES20.glDeleteShader(vShaderId)
        GLES20.glDeleteShader(fShaderId)


        //五、获取着色器程序变量的索引，通过索引来赋值
        //1. 顶点各个变量的索引
        vPosition = GLES20.glGetAttribLocation(mProgramId, "vPosition")
        vCoord = GLES20.glGetAttribLocation(mProgramId, "vCoord")
        vMatrix = GLES20.glGetUniformLocation(mProgramId, "vMatrix")

        //2、片元各个变量的索引
        vTexture = GLES20.glGetUniformLocation(mProgramId, "vTexture")

        //六、赋值
        //1、顶点坐标赋值
        mVertexBuffer = ByteBuffer
            .allocateDirect(4 * 4 * 2)     // 屏幕一共4个点，每个点有x/y两个坐标，坐标是Float类型，4个字节，则为4*4*2
            .order(ByteOrder.nativeOrder())     //设置使用硬件本地字节序列，保证数据排序
            .asFloatBuffer()                    //创建FloatBuffer
        mVertexBuffer.clear()

        //采用OpenGL的坐标系
        //屏幕边缘4个点
        //不能采用顺时针或逆时针依次获取4个点，因为这样无法无法组成闭合的矩形
        // 例如顺时针四个点分别为 a b c d，则输入进去的点为a b d c
        val v = floatArrayOf(
            -1f, 1f,
            -1f, -1f,
            1f, 1f,
            1f, -1f
        )
        mVertexBuffer.put(v)


        //2、纹理坐标赋值
        mTextureBuffer = ByteBuffer
            .allocateDirect(4 * 4 * 2)     // 屏幕一共4个点，每个点有x/y两个坐标，坐标是Float类型，4个字节，则为4*4*2
            .order(ByteOrder.nativeOrder())     //设置使用硬件本地字节序列，保证数据排序
            .asFloatBuffer()                    //创建FloatBuffer
        mTextureBuffer.clear()

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
        val t = floatArrayOf(
            1f, 1f,
            0f, 1f,
            1f, 0f,
            0f, 0f
        )
        mTextureBuffer.put(t)

    }
}