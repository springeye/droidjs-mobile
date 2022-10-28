package com.github.springeye.droidjs.runtime

import android.util.Log
import com.github.springeye.droidjs.ScriptRuntime
import java.io.File
import javax.inject.Inject

class NodeScriptRuntime @Inject constructor(): ScriptRuntime {
    external suspend fun startNodeWithArguments(arguments:Array<String>):Int
    companion object{
        init {
            System.loadLibrary("droidjs")
            System.loadLibrary("node")
        }
    }



    override suspend fun exec(script: String, type: ScriptRuntime.Type): Any? {
        Log.d("NodeJSRuntime","执行脚本....")
        return startNodeWithArguments(arrayOf("node","-e",script))
    }

    override suspend fun run(file: File, type: ScriptRuntime.Type) {
        startNodeWithArguments(arrayOf("node",file.toString()))
    }

    override suspend fun close() {
        TODO("Not yet implemented")
    }
}