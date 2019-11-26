package com.hujun.opengldemo.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Created by junhu on 2019-11-26
 */
class BufferHelper {
    companion object {
        fun getFloatBuffer(vertexes: FloatArray): FloatBuffer {
            //分配一块本地内存，不受GC管理
            val buffer = ByteBuffer
                .allocateDirect(vertexes.size * 4) //一共有size个点，每个点4个字节
                .order(ByteOrder.nativeOrder())     //设置使用设备硬件的本地字节序，保证数据排序一致性
                .asFloatBuffer()        //从ByteBuffer中创建一个浮点型缓冲器
                .put(vertexes)          //写入顶点数组
            buffer.clear()
            return buffer
        }
    }
}