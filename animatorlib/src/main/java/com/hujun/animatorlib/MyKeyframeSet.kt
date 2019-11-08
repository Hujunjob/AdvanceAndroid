package com.hujun.animatorlib

import android.animation.TypeEvaluator

/**
 * Created by junhu on 2019-11-08
 */
class MyKeyframeSet private constructor(){
    //类型估值器
    lateinit var mEvaluator:TypeEvaluator<Any>

    //关键帧，初始帧
    lateinit var mFirstKeyframe:MyFloatKeyframe

    var myKeyframes : ArrayList<MyFloatKeyframe>

    init {
        myKeyframes = arrayListOf()

    }

    companion object{
        fun ofFloat(vararg values:FloatArray):MyKeyframeSet{
            var numFrames = values.size
            //关键帧初始化

        }
    }
}