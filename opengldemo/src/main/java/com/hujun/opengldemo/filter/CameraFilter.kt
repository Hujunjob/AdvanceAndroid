package com.hujun.opengldemo.filter

import android.content.Context
import android.opengl.GLES20
import com.hujun.opengldemo.R

/**
 * Created by junhu on 2019-11-26
 */
class CameraFilter(
    context: Context,
    vertexSourceId: Int = R.raw.camera_vertex,
    fragmentSourceId: Int = R.raw.camera_fragment
) :
    BaseFilter(context, vertexSourceId, fragmentSourceId) {

    override fun onReady(width: Int, height: Int) {
        super.onReady(width, height)

    }

}