package com.github.springeye.droidjs

interface LuaRuntime {
    fun exec(script:String):Any
    fun close()
}