package com.github.springeye.droidjs

interface JSRuntime {
    fun exec(script:String):Any?
    fun close()
}