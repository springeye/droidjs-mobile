package com.github.springeye.droidjs.base

import java.io.File

interface ScriptRuntime {
    enum class Type{
        JS,LUA
    }
    suspend  fun exec(script:String,type: Type):Any?
    suspend  fun run(file: File,type: Type)
    suspend fun close()
}