package com.hujun.dispatchevent

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup

/**
 * Created by junhu on 2019-11-09
 */
class TouchViewGroup(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }


}