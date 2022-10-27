package com.github.springeye.droidjs.utils

/**
 * Created by Stardust on 2017/11/26.
 */
object Nath {
    fun min(vararg ints: Int): Int {
        var min = ints[0]
        for (i in 1 until ints.size) {
            min = if (ints[i] < min) ints[i] else min
        }
        return min
    }
}