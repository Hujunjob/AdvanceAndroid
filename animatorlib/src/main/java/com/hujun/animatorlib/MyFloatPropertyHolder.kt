package com.hujun.animatorlib

import java.lang.reflect.Method

/**
 * Created by junhu on 2019-11-08
 */
class MyFloatPropertyHolder internal constructor(propertyName: String, values: FloatArray) {
    //属性名
    var mPropertyName: String
    //设置的属性，float
    var mValueType: Class<Float.Companion>
    //反射获得方法
    lateinit var mSetterMethod: Method

    //关键帧管理类
    var mKeyframes: MyKeyframeSet

    init {
        this.mPropertyName = propertyName
        mValueType = Float.javaClass
        mKeyframes = MyKeyframeSet.ofFloat(values)

    }

}