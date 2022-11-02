package com.github.springeye.droidjs.luaj

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class SystemFunctions {
    companion object{
        const val LOG_TAG="SystemFunctions"
    }
    fun sleep(timeMillis: Long){
        Log.d(LOG_TAG,"sleep $timeMillis")
        runBlocking {
            delay(timeMillis)
        }
    }
}