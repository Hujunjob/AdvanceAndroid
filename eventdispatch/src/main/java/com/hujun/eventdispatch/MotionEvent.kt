package com.hujun.eventdispatch

/**
 * Created by junhu on 2019-11-09
 */
class MotionEvent constructor(var x: Float, var y: Float, var actionMasked: Int) {

    constructor() : this(0f, 0f, 0)

    constructor(x: Float, y: Float) : this(x, y, 0)

    companion object {
        val ACTION_DOWN = 0

        val ACTION_UP = 1

        val ACTION_MOVE = 2

        val ACTION_CANCEL = 3
    }
}