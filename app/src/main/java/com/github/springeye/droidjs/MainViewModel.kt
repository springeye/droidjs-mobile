package com.github.springeye.droidjs

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.eclipsesource.v8.V8ScriptExecutionException
import com.github.springeye.droidjs.proto.ProtoMessage
import com.github.springeye.droidjs.runtime.JSRuntimeV8
import com.github.springeye.droidjs.runtime.NodeJSRuntime
import com.github.springeye.droidjs.utils.copyFileOrDir
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: DroidJsApplication,
                                        val lua: LuaRuntime,
                                        val js: JSRuntimeV8,
                                        val node: NodeJSRuntime,
) : AndroidViewModel(application) {

    fun executeTest(){
        try {
//            ProtoMessage.RegisterRequest.newBuilder().setEmail("").setUsername("").setPassword("").build()
//            val script = getApplication<DroidJsApplication>().assets.open("tests/app.js").bufferedReader().use {
//                it.readText()
//            }
//            js.exec(script)
//            js.exec("image.read('./test.png');")
//            js.exec("const resolve = require('path').resolve;console.log(resolve('./test.png'));")


            viewModelScope.launch(Dispatchers.IO){
//                val script = getApplication<DroidJsApplication>().assets.open("tests/http_server.js").bufferedReader().use {
//                    it.readText()
//                }
//                js.exec(script)


                val app = getApplication<DroidJsApplication>()
                app.copyFileOrDir("examples/nodejs")
                node.run(File(app.filesDir,"examples/nodejs/").toString())
            }

        } catch (e: Exception) {
            if(e is V8ScriptExecutionException){
                Log.w("MainActivity","${e.message}\n${e.sourceLine}\n${e.jsStackTrace}"+"\n\n")
//                e.printStackTrace()
            }else {
                Log.e("MainActivity", "执行js出现错误", e)
            }
        }
    }
}