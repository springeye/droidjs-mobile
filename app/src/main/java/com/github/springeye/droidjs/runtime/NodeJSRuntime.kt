package com.github.springeye.droidjs.runtime

import android.util.Log
import com.github.springeye.droidjs.JSRuntime
import javax.inject.Inject

class NodeJSRuntime @Inject constructor(): JSRuntime {
    external suspend fun startNodeWithArguments(arguments:Array<String>):Int
    companion object{
        init {
            System.loadLibrary("droidjs")
            System.loadLibrary("node")
        }
    }

    override suspend fun exec(script: String): Any? {
        Log.d("NodeJSRuntime","执行脚本....")
        return startNodeWithArguments(arrayOf("node","-e",script))
    }
    suspend fun run(dir:String): Int {
        return startNodeWithArguments(arrayOf("node",dir))
    }
    override suspend fun close() {
        TODO("Not yet implemented")
    }
}