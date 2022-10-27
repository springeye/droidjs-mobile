package com.github.springeye.droidjs

interface LuaRuntime {
    suspend fun exec(script:String):Any
    suspend fun close()
}