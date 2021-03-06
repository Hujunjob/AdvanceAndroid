package com.hujun.opengldemo

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import com.hujun.slamdemo.CameraFactory

/**
 * Created by junhu on 2019-11-25
 */
class MyGLSurface(context: Context?, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {
    init {
        //1、设置EGL版本
        setEGLContextClientVersion(2)

        //2、设置渲染器
        setRenderer(MyGLRenderer(this))

        //3、设置渲染模式
        renderMode = RENDERMODE_WHEN_DIRTY

    }

    companion object{
        private val TAG = this::class.java.name.replace("${'$'}Companion","").split(".").last()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        super.surfaceDestroyed(holder)
        Log.d(TAG, "surfaceDestroyed: ")
        CameraFactory.cameraEngine.release()
    }
}