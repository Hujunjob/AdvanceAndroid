package com.hujun.eventdispatch

/**
 * Created by junhu on 2019-11-09
 */
class ViewGroup: View() {
    //存放子控件
     var childList:ArrayList<View> = ArrayList()



    fun addView(view: View){
        if (view==null) {
            return
        }

        childList.add(view)
    }


    fun onInterceptTouchEvent(event: MotionEvent):Boolean{
        return false
    }

}