package com.hujun.myapplication.utils

import android.content.Context
import java.io.InputStreamReader
import java.lang.StringBuilder

/**
 * Created by junhu on 2019-11-29
 */
class ShaderResourceLoader {
    companion object {
        fun loadResource(context: Context, resourceId: Int): String {
            var inputStream = context.resources.openRawResource(resourceId)
            var inputStreamReader = InputStreamReader(inputStream)
            var sb = StringBuilder()
            inputStreamReader.forEachLine {
                sb.append(it)
                sb.append("\n")
            }
            return sb.toString()
        }
    }
}