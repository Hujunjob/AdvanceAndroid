package com.hujun.myapplication.utils

import com.hujun.myapplication.utils.Constants.Companion.SIZE_OF_FLOAT
import com.hujun.myapplication.utils.Constants.Companion.SIZE_OF_INT
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Created by junhu on 2019-11-29
 */
class BufferHelper {
    companion object{
        fun generateBuffer(vertex:FloatArray):FloatBuffer{
            val buffer = ByteBuffer.allocateDirect(vertex.size * SIZE_OF_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex)
            buffer.clear()
            return buffer
        }

        fun generateBuffer(vertex:IntArray):IntBuffer{
            val buffer = ByteBuffer.allocateDirect(vertex.size * SIZE_OF_INT)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(vertex)
            buffer.clear()
            return buffer
        }
    }
}