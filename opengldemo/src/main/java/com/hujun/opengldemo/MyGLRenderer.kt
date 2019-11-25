package com.hujun.opengldemo

import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.hujun.opengldemo.filter.ScreenFilter
import com.hujun.slamdemo.CameraEngine
import com.hujun.slamdemo.CameraFactory
import com.hujun.slamdemo.ICameraEngine
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 核心类
 */
class MyGLRenderer(var myGLSurface: MyGLSurface) : GLSurfaceView.Renderer,
    SurfaceTexture.OnFrameAvailableListener {
    private lateinit var mTextureIds: IntArray
    private lateinit var screenFilter: ScreenFilter
    var cameraEngine: ICameraEngine? = null
    var mSurfaceTexture:SurfaceTexture? = null

    var mtx = FloatArray(16)

    override fun onDrawFrame(gl: GL10?) {
        //在这里画画
        //1、清空画布，设置画布颜色，这里设置为红色
        GLES20.glClearColor(255f,0f,0f,0f)
        //设置清理模式
        //GL_COLOR_BUFFER_BIT 颜色缓存区
        //GL_DEPTH_BUFFER_BIT 深度缓冲区
        //GL_STENCIL_BUFFER_BIT 模型缓冲区
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT)

        //2、输出摄像头数据
        //更新纹理
        mSurfaceTexture?.updateTexImage()
        //获取旋转矩阵
        mSurfaceTexture?.getTransformMatrix(mtx)

        screenFilter.onDrawFrame(mTextureIds[0],mtx)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        cameraEngine?.openCamera(true)

        //设置gl窗口
//        GLES20.glViewport(0,0,width,height)

        screenFilter.onReady(width,height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        cameraEngine =
            CameraFactory.createCameraEngine(CameraFactory.CameraType.CAMERA1, myGLSurface.context)

        //准备画布
        mTextureIds = IntArray(1)
        //通过OpenGL创建纹理id
        GLES20.glGenTextures(mTextureIds.size,mTextureIds,0)
        //通过纹理id创建画布
        mSurfaceTexture = SurfaceTexture(mTextureIds[0])
        mSurfaceTexture?.setOnFrameAvailableListener(this)
        cameraEngine?.addSurfaceView(mSurfaceTexture!!)

        screenFilter = ScreenFilter(myGLSurface.context)


    }

    //画布有可用数据时回调
    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        //请求渲染
        myGLSurface.requestRender()
    }

}
