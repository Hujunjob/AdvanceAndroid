package com.hujun.opengldemo

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        imagebutton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> mysurface.stopRecording()
                MotionEvent.ACTION_DOWN -> mysurface.startRecording(getRecordType())
                MotionEvent.ACTION_CANCEL -> mysurface.stopRecording()
            }
            return@setOnTouchListener true
        }
    }

    private fun getRecordType(): MyGLSurface.RecordType {
        when (radiogroup.checkedRadioButtonId) {
            R.id.radio_fast -> return MyGLSurface.RecordType.FAST
            R.id.radio_very_fast -> return MyGLSurface.RecordType.VERY_FAST
            R.id.radio_normal -> return MyGLSurface.RecordType.NORMAL
            R.id.radio_slow -> return MyGLSurface.RecordType.SLOW
            R.id.radio_very_slow -> return MyGLSurface.RecordType.VERY_SLOW
        }
        return MyGLSurface.RecordType.NORMAL
    }

    @SuppressLint("NewApi")
    private fun checkPermissions() {
        var needPermission = false
        permissions.forEach {
            if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                needPermission = true
            }
        }
        if (needPermission)
        requestPermissions(permissions, 1)

    }
}
