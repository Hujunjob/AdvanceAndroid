package com.hujun.slamdemo

import android.content.Context

/**
 * Created by junhu on 2019-11-14
 */
object CameraFactory {
    enum class CameraType {
        CAMERA1,
        CAMERA2,
        CAMERAx
    }

    lateinit var cameraEngine: ICameraEngine

    fun createCameraEngine(cameraType: CameraType, context: Context): ICameraEngine? {
        when (cameraType) {
            CameraType.CAMERA1 -> {
                cameraEngine = CameraEngine.getInstance(context)
            }
            CameraType.CAMERA2 -> {
//                cameraEngine = Camera2Engine.getInstance(context)
            }
            CameraType.CAMERAx -> {
//                cameraEngine = CameraSource.Instance()
            }
        }
        return cameraEngine
    }
}