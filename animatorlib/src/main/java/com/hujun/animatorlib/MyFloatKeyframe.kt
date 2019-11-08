package com.hujun.animatorlib

/**
 * Created by junhu on 2019-11-08
 */
class MyFloatKeyframe internal constructor(
    //当前运行百分比，0~1
    var mFraction: Float,

    //当前值
    var mValue: Float) {

    private var mValueType: Class<Any> = Float.javaClass



}