package com.hujun.opengldemo

import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.hiscene.agoraengine.opengl.ShaderFilter
import android.opengl.*
import android.util.Log
import com.hujun.myapplication.entity.Triangle
import com.hujun.myapplication.utils.MathUtils
import com.hujun.myapplication.utils.ShaderProgram
import com.hujun.opengldemo.entity.Cube
import com.hujun.opengldemo.filter.CameraFilter
import com.hujun.opengldemo.filter.ScreenFilter
import com.hujun.slamdemo.CameraFactory
import com.hujun.slamdemo.ICameraEngine
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 核心类
 */
class MyGLRenderer(var myGLSurface: MyGLSurface) : GLSurfaceView.Renderer,
    SurfaceTexture.OnFrameAvailableListener {
    private lateinit var mCameraFilter: CameraFilter
//    private lateinit var mTextureIds: IntArray
    private var mCameraTexureId = 0
    private lateinit var mScreenFilter: ScreenFilter
    var cameraEngine: ICameraEngine? = null
    var mSurfaceTexture: SurfaceTexture? = null
    private var triangle: Triangle? = null
    private var cube: Cube? = null
    var mtx = FloatArray(16)
    // Get projection matrix.
    // Get projection matrix.
    var projmtx = FloatArray(16)

    // Get camera matrix and draw.
    // Get camera matrix and draw.
    var viewmtx = FloatArray(16)

    private val mScaleFactor = 1.0f

    private lateinit var shaderFilter:ShaderFilter

    // Temporary matrix allocated here to reduce number of allocations for each frame.
    private val mAnchorMatrix = FloatArray(16)

    companion object{
        private val TAG = this::class.java.name.replace("${'$'}Companion","").split(".").last()

    }


    override fun onDrawFrame(gl: GL10?) {
        //在这里画画
        //1、清空画布，设置画布颜色，这里设置为红色
        GLES20.glClearColor(255f, 0f, 0f, 0f)
        //设置清理模式
        //GL_COLOR_BUFFER_BIT 颜色缓存区
        //GL_DEPTH_BUFFER_BIT 深度缓冲区
        //GL_STENCIL_BUFFER_BIT 模型缓冲区
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

        //2、输出摄像头数据
        //更新纹理
        mSurfaceTexture?.updateTexImage()
        //获取旋转矩阵
        mSurfaceTexture?.getTransformMatrix(mtx)

        //mTextureIds[0]是摄像头的渲染纹理
        //先把数据渲染到FBO
        mCameraFilter.mtx = mtx
        val textureId = mCameraFilter.onDrawFrame(mCameraTexureId)

//        triangle?.draw()

        cube?.draw()

        checkGLError("1")

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        //解绑纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
        checkGLError("2")
        //采用类似链式调用
        //每个Filter都会在上一个传递过来的textureId渲染数据
        //然后将textureId传递下去，继续渲染
        //最后用ScreenFilter将textureId渲染到屏幕上
//        mScreenFilter.onDrawFrame(textureId)

    }


//        shaderFilter.draw()
        // Update and draw the model and its shadow.
//        mVirtualObject.updateModelMatrix(mAnchorMatrix, mScaleFactor)
//        mVirtualObject.draw(viewmtx, projmtx, 1f)
    fun checkGLError(msg: String = "") {
        var error = GLES32.glGetError()
        if (error != GLES32.GL_NO_ERROR) {
            Log.e(TAG, "$msg checkGLError: ${MathUtils.intToHex(error)}")
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        cameraEngine?.openCamera(true)

        mScreenFilter.onReady(width, height)
        mCameraFilter.onReady(width, height)

        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        cameraEngine =
            CameraFactory.createCameraEngine(CameraFactory.CameraType.CAMERA1, myGLSurface.context)

        //准备画布
        var mTextureIds = IntArray(1)
        //通过OpenGL创建纹理id
        GLES20.glGenTextures(1, mTextureIds, 0)

        mCameraTexureId = mTextureIds[0]

        //通过纹理id创建画布
//        mSurfaceTexture = SurfaceTexture(mTextureIds[0])

        mSurfaceTexture = SurfaceTexture(mCameraTexureId)
        mSurfaceTexture?.setOnFrameAvailableListener(this)
        cameraEngine?.addSurfaceView(mSurfaceTexture!!)

        mScreenFilter = ScreenFilter(myGLSurface.context)
        mCameraFilter = CameraFilter(myGLSurface.context)

        shaderFilter = ShaderFilter(myGLSurface.context)
        // Prepare the other rendering objects.
        // Prepare the other rendering objects.
//        try {
//            mVirtualObject.createOnGlThread( /*context=*/myGLSurface.context, "andy.obj", "andy.png")
//            mVirtualObject.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f)
//        } catch (e: IOException) {
//            Log.e(TAG, "Failed to read obj file")
//        }
        generateTriangle()
        generateCube()
    }



    //画布有可用数据时回调
    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        //请求渲染
        myGLSurface.requestRender()
    }


    private fun generateTriangle() {
        val triangleShader = ShaderProgram(
            myGLSurface.context,
            R.raw.triangle_vertex,
            R.raw.triangle_fragment
        )
        triangleShader.linkProgram()
        triangle = Triangle(triangleShader.mProgram)
    }


    private fun generateCube() {
        val cubeProgram = ShaderProgram(
            myGLSurface.context,
            R.raw.cube_vertex, R.raw.cube_fragment
        )
        cubeProgram.linkProgram()
        cube = Cube(myGLSurface.context, cubeProgram.mProgram)
    }

}
