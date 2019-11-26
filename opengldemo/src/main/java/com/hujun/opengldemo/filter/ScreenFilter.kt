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
class ScreenFilter(
    context: Context,
    vertexSourceId: Int = R.raw.base_vertex,
    fragmentSourceId: Int = R.raw.base_fragment
) :
    BaseFilter(context, vertexSourceId, fragmentSourceId) {




}