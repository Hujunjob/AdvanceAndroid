package com.hujun.animatorlib

import android.view.View
import java.lang.ref.WeakReference

/**
 * Created by junhu on 2019-11-08
 */
class MyObjectAnimator private constructor(var target: View,var propertyName: String, var values: FloatArray) {
    private val TAG = "MyObjectAnimator"
    private var mStartTime = -1L
    private var mDuration = 0L
    private var mTarget: WeakReference<View>
    private var myFloatPropertyHolder:MyFloatPropertyHolder

    init {
        mTarget = WeakReference(target)
        myFloatPropertyHolder = MyFloatPropertyHolder(propertyName,values)
    }

    companion object {

        fun ofFloat(target: View, propertyName: String, vararg values: Float): MyObjectAnimator {
            var animator = MyObjectAnimator(target,propertyName, values)
            return animator
        }
    }
}