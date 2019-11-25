package com.hujun.opengldemo.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder

/**
 * Created by junhu on 2019-11-25
 */
class TextResourceReader {
    companion object {
        fun read(context: Context, resourceId: Int):String {
            val sb = StringBuilder()
            val inputStream = context.resources.openRawResource(resourceId)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferReader = BufferedReader(inputStreamReader)
            var s = bufferReader.readLine()
            while (s!=null){
                sb.append(s)
                sb.append("\n")
                s = bufferReader.readLine()
            }
            return sb.toString()
        }
    }
}