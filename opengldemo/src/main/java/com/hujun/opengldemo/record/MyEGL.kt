package com.hujun.opengldemo.record

import android.content.Context
import android.opengl.*
import android.opengl.EGL14.*
import android.view.Surface
import com.hujun.myapplication.utils.MathUtils
import com.hujun.opengldemo.filter.ScreenFilter

/**
 * Created by junhu on 2020-01-08
 * 配置EGL环境
 */
class MyEGL(eglContext: EGLContext, surface: Surface, context: Context, width: Int, height: Int) {
    private lateinit var mEglDisplay: EGLDisplay
    private lateinit var mEGLConfig: EGLConfig
    private lateinit var mEGLContext: EGLContext
    private lateinit var mEGLSurface: EGLSurface
    private lateinit var mScreenFilter: ScreenFilter

    init {
        //1、创建EGL环境
        createEGL(eglContext)

        val attrib_list = intArrayOf(
            EGL_NONE
        )
        //2、创建窗口，绘制线程中的图像，就是往这里创建的mEGLSurface上去画
        mEGLSurface = eglCreateWindowSurface(
            mEglDisplay,
            mEGLConfig,
            surface,
            attrib_list,
            0
        )

        //3、让surface和egldisplay绑定
        var res = eglMakeCurrent(mEglDisplay, mEGLSurface, mEGLSurface, mEGLContext)
        if (!res) {
            throw RuntimeException("eglMakeCurrent fail "+ eglGetError())
        }

        //往虚拟屏幕上画画
        mScreenFilter = ScreenFilter(context)
        mScreenFilter.onReady(width, height)
    }

    private fun createEGL(eglContext: EGLContext) {
        //1. 获取显示设备
        mEglDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY)

        val version = IntArray(2)
        //2. 初始化设备
        var res = eglInitialize(
            mEglDisplay,
            version,
            0,
            version,
            1
        )
        if (!res) {
            throw RuntimeException("eglInitialize失败 "+ eglGetError())
        }

        val attrib_list = intArrayOf(
            //指定像素格式rgba
            EGL_RED_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_BLUE_SIZE, 8,
            EGL_ALPHA_SIZE, 8,
            //指定渲染api类型
            EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT, //opengl es 版本号
            //告诉EGL，创建surface的行为必须是视频编解码器所能兼容的
            EGLExt.EGL_RECORDABLE_ANDROID, 1,
            EGL_NONE //结尾符
        )

        val configs = arrayOfNulls<EGLConfig?>(1)

        val num_config = IntArray(1)

        //3. 选择配置
        res = eglChooseConfig(
            mEglDisplay,
            attrib_list,
            0,
            configs,
            0,
            configs.size,
            num_config,
            0
        )
        if (!res) {
            throw RuntimeException("eglInitialize失败 "+ eglGetError())
        }

        mEGLConfig = configs[0]!!

        //context的属性列表
        val attrib_list2 = intArrayOf(
            EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL_NONE
        )

        //4. 创建上下文
        mEGLContext = eglCreateContext(
            mEglDisplay,
            mEGLConfig,
            eglContext,   //共享上下文，传递绘制线程中GLThread的EGL上下文，共享资源
            attrib_list2,
            0
        )

        if (mEGLContext == EGL_NO_CONTEXT) {
            throw RuntimeException("eglCreateContext fail "+ MathUtils.intToHex(eglGetError()))
        }

    }

    fun draw(textId: Int, time: Long) {
        //渲染到虚拟屏幕
        mScreenFilter.onDrawFrame(textId)

        //刷新surface时间戳，如果设置不合理，编码的时候会丢帧或者以低质量的编码方式进行编码
        EGLExt.eglPresentationTimeANDROID(mEglDisplay, mEGLSurface, time)

        //交换缓冲数据
        eglSwapBuffers(mEglDisplay, mEGLSurface)
    }

    /**
     * 释放资源
     */
    fun release() {
        eglMakeCurrent(mEglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT)
        eglDestroySurface(mEglDisplay, mEGLSurface)
        eglDestroyContext(mEglDisplay, mEGLContext)
        eglReleaseThread()
        eglTerminate(mEglDisplay)
    }
}