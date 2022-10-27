package com.github.springeye.droidjs

interface JSRuntime {
    suspend  fun exec(script:String):Any?
    suspend fun close()
}