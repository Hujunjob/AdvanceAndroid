package com.hujun.dispatchevent

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by junhu on 2019-11-08
 */
class TouchView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        println("onTouchEvent ${event?.action},${this.hashCode()}")
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(event)
    }
}