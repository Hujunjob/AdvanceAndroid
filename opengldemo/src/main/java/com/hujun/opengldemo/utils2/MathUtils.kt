package com.hujun.myapplication.utils

/**
 * Created by junhu on 2019-11-30
 */
class MathUtils {
    companion object {
        //10进制转16进制
        fun intToHex(n: Int): String? { //StringBuffer s = new StringBuffer();
            var n = n
            var sb = StringBuilder(8)
            val a: String
            val b = charArrayOf(
                '0',
                '1',
                '2',
                '3',
                '4',
                '5',
                '6',
                '7',
                '8',
                '9',
                'A',
                'B',
                'C',
                'D',
                'E',
                'F'
            )
            while (n != 0) {
                sb = sb.append(b[n % 16])
                n = n / 16
            }
            a = sb.reverse().toString()
            return a
        }

    }
}