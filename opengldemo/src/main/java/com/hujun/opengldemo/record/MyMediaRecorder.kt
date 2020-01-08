package com.hujun.opengldemo.record

import android.content.Context
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.opengl.EGLContext
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import java.lang.RuntimeException

/**
 * Created by junhu on 2020-01-08
 */
class MyMediaRecorder(
    var mWidth: Int,
    var mHeight: Int,
    var mOutputPath: String,
    var mContext: Context,
    var mEGLContext: EGLContext
) {
    companion object {
        private val TAG = this::class.java.name.replace("${'$'}Companion", "").split(".").last()
    }

    private var index: Int = 0
    private var mMediaCoder: MediaCodec? = null
    private var mMediaMuxer: MediaMuxer? = null
    private var mInputSurface: Surface? = null
    private var recordHandler: Handler? = null
    private var isStart = false
    private var myEGL: MyEGL? = null

    private var recordSpeed = 1f

    fun startRecord() {
        //1、创建视频编码器，MIMETYPE_VIDEO_AVC为H.264
        mMediaCoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)

        //2、配置编码器
        val videoFormat =
            MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, mWidth, mHeight)

        /**
         * 比特率计算方法：pixel count * fps * quality factor * 0.07 / 1000 = bit rate in kbps
         * pixel count = width * height
         * quality factor 是质量系数，比如标清、高清、超清，分别为1、2、4
         * 1280*720 15fps 标清
         * bitrate = 1280 * 720 * 15 * 1 * 0.07 / 1000 = 976kbps
         * bitrate = 1080 * 1920 * 30 * 1 * 0.07 / 1000 = 4354kbps
         */
        //设置比特率，
        val fps = 30
        val quality = 1
        val bitrate = (1080 * 1920 * fps * quality * 0.07 ).toInt()
        Log.d(TAG, "startRecord: fps=$fps,quality=$quality,bitrate=$bitrate")
        videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitrate)
        //设置帧率
        videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, fps)

        //颜色格式，从Surface中动态获取
        videoFormat.setInteger(
            MediaFormat.KEY_COLOR_FORMAT,
            MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
        )
        //关键帧间隔,设置20s
        videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 20)

        mMediaCoder?.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)

        //3、创建输入surface，虚拟屏
        mInputSurface = mMediaCoder?.createInputSurface()

        //4、创建封装器（复用器）
        mMediaMuxer = MediaMuxer(mOutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

        //5、配置EGL环境
        //两个线程，渲染线程和录制线程
        val recordThread = HandlerThread("MyMediaRecorder")
        recordThread.start()
        recordHandler = Handler(recordThread.looper)
        recordHandler?.post {
            myEGL = MyEGL(
                mEGLContext,
                mInputSurface!!,
                mContext,
                mWidth,
                mHeight
            )
            mMediaCoder?.start()
            isStart = true
        }

    }

    fun stopRecord() {
        isStart = false

        recordHandler?.post {
            getEncodedData(true)
            mMediaCoder?.stop()
            mMediaCoder?.release()

            mMediaMuxer?.stop()
            mMediaMuxer?.release()

            mInputSurface?.release()

            myEGL?.release()
            recordHandler?.looper?.quitSafely()
            recordHandler = null
        }
    }

    fun encodeFrame(textureId: Int, time: Long) {
        if (!isStart) {
            return
        }
        recordHandler?.post {
            //传递数据，进行编码
            myEGL?.draw(textureId, time)

            //编码后，从编码器中的输出缓冲区获取编码后的数据
            getEncodedData(false)
        }
    }


    /**
     * 获取编码后的数据
     * @param endOfStream 是否结束录制
     */
    private fun getEncodedData(endOfStream: Boolean) {
        if (endOfStream) {
            //发送结束消息给编码器
            mMediaCoder?.signalEndOfInputStream()
        }

        val bufferInfo = MediaCodec.BufferInfo()
        while (true) {
            val status = mMediaCoder?.dequeueOutputBuffer(bufferInfo, 10_000)!!
            if (status == MediaCodec.INFO_TRY_AGAIN_LATER) {
                //
                if (!endOfStream) {
                    break
                }
            } else if (status == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                index = mMediaMuxer?.addTrack(mMediaCoder?.outputFormat)!!
                //开始封装
                mMediaMuxer?.start()
            } else if (status == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {

            } else {
                //成功取出一个有效的数据
                val byteBuffer = mMediaCoder?.getOutputBuffer(status)
                    ?: throw RuntimeException("getEncodedData fail")

                //取到bytebuffer是h264配置信息
                if ((bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    bufferInfo.size = 0
                }

                if (bufferInfo.size != 0) {
                    //修改bufferInfo的时间戳，用来控制录制速度

//                    Log.d(TAG, "getEncodedData: time=${bufferInfo.presentationTimeUs}")

                    bufferInfo.presentationTimeUs = (bufferInfo.presentationTimeUs.toDouble()/recordSpeed.toDouble()).toLong()

//                    Log.d(TAG, "getEncodedData: new time=${bufferInfo.presentationTimeUs}")

                    byteBuffer.position(bufferInfo.offset)
                    byteBuffer.limit(bufferInfo.size + bufferInfo.offset)

                    //输出数据
                    mMediaMuxer?.writeSampleData(index, byteBuffer, bufferInfo)
                }
                //要释放数据，使用完缓冲区，立即回收，让MediaCodec能继续使用
                mMediaCoder?.releaseOutputBuffer(status, false)

                //结束
                if ((bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    break
                }
            }
        }
    }

    fun setRecordSpeed(speed: Float) {
        recordSpeed = speed
    }
}