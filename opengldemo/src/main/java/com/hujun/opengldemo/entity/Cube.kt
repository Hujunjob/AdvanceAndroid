package com.hujun.opengldemo.entity

import android.content.Context
import android.opengl.GLES30
import android.opengl.Matrix
import com.hujun.myapplication.entity.BaseEntity
import com.hujun.myapplication.utils.TextureHelper
import com.hujun.opengldemo.R


/**
 * Created by junhu on 2019-12-14
 */
class Cube(context: Context, program: Int) : BaseEntity(program) {
    private var textureId = 0

    //projection * view * model * transform
    private var projectionUniform = 0
    private var viewUniform = 0
    private var modelUniform = 0
    private var transformUniform = 0

    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
//    private val transformMatrix = FloatArray(16)

    init {
        initTexture(context)
        initUniform()
        afterInit()
    }

    private fun initTexture(context: Context) {
        textureId = TextureHelper.generateTexture(context, R.mipmap.container)
        setUniform1i("ourTexture", 0)

        checkGLError("initTexture")
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
    }

    private fun initUniform() {
        projectionUniform = GLES30.glGetUniformLocation(mProgram, "projection")
        viewUniform = GLES30.glGetUniformLocation(mProgram, "view")
        modelUniform = GLES30.glGetUniformLocation(mProgram, "model")
        transformUniform = GLES30.glGetUniformLocation(mProgram, "transform")

        Matrix.setIdentityM(projectionMatrix, 0)
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.setIdentityM(modelMatrix, 0)
//        Matrix.setIdentityM(transformMatrix,0)

        setProjection()
        setViewMatrix()
        setModelMatrix()
    }

    private fun setProjection() {
        //投影矩阵，透视投影
        val fov = 45.0f
        val respect = 720.0f / 1280.0f
        val near = 1f
        val far = 100.0f
//        return glm::perspective(glm::radians(fov), respect, near, far);
        Matrix.perspectiveM(projectionMatrix, 0, fov, respect, near, far)
    }

    private fun setViewMatrix() {
        //观察矩阵，就是摄像机矩阵
        //相机往前移动一段距离观测，相当于整个场景往后移动
        Matrix.translateM(viewMatrix, 0, 0f, 0f, -3f)
    }

    private fun setModelMatrix() {
        //模型矩阵，就是模型自己在自己的坐标系里变换了
        //模型矩阵，绕X轴旋转55°，相当于平躺55°下来
        Matrix.rotateM(modelMatrix, 0, -55f, 1f, 0f, 0f)
    }

    private var frameCount=0
    override fun draw() {
        checkGLError("draw before")
        GLES30.glUseProgram(mProgram)
        GLES30.glBindVertexArray(VAO)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)


        frameCount++

        Matrix.rotateM(modelMatrix, 0, Math.toRadians(frameCount*0.3).toFloat(), 0f, 0f, 1f)


        GLES30.glUniformMatrix4fv(projectionUniform, 1, false,projectionMatrix, 0)
        GLES30.glUniformMatrix4fv(modelUniform, 1,false, modelMatrix, 0)
        GLES30.glUniformMatrix4fv(viewUniform, 1,false, viewMatrix, 0)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36)
        GLES30.glBindVertexArray(0)
        checkGLError("draw after")
    }


    override fun initVertex() {
        mVertex = floatArrayOf(
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
        )

    }

    override fun handleVBO() {

        //传递位置数组
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 5 * SIZE_OF_FLOAT, 0)
        GLES30.glEnableVertexAttribArray(0)

        //传递纹理数组
        GLES30.glVertexAttribPointer(2, 2, GLES30.GL_FLOAT, false, 5 * SIZE_OF_FLOAT,
                3 * SIZE_OF_FLOAT)
        GLES30.glEnableVertexAttribArray(2)
    }

}