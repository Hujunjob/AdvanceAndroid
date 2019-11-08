package com.hujun.dispatchevent

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var animator = ObjectAnimator.ofFloat(btn1,"scaleX",1f,2f)
        animator.duration = 3000
        animator.interpolator = LinearInterpolator()
        animator.start()
    }
}
