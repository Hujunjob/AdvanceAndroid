package com.hujun.slamdemo

import android.content.Context
import android.graphics.SurfaceTexture
import android.view.View

import java.nio.ByteBuffer

/**
 * @author: liwenfei.
 * data: 2018/11/8 20:20.
 */
interface ICameraEngine {
    interface OnNewFrameListener {
        fun onNewFrame(data: ByteArray, width: Int, height: Int)
        fun onBuffer(
            yBuffer: ByteBuffer,
            uBuffer: ByteBuffer,
            vBuffer: ByteBuffer,
            width: Int,
            height: Int
        )
        fun onError(error: Int)
    }

    fun setResolution(width: Int, height: Int)

    fun openCamera(isFront: Boolean): Boolean

    fun closeCamera(): Boolean

    fun switchCamera(): Boolean

    fun setOnNewFrameListener(onNewFrame: OnNewFrameListener)

    fun release()

    fun addSurfaceView(surfaceTexture: SurfaceTexture)

    fun removeSurfaceView(surfaceTexture: SurfaceTexture)

    fun setPreviewView(view: View)

    fun onStart()

    fun onStop()

    fun getPreviewWidth():Int
    fun getPreviewHeight():Int
}
