package com.github.springeye.droidjs.v8

import android.widget.Toast
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.JSRuntime
import com.github.springeye.droidjs.jsmodules.App
import com.github.springeye.droidjs.jsmodules.Console
import com.github.springeye.droidjs.jsmodules.IApp
import com.github.springeye.droidjs.jsmodules.IConsole

class JSRuntimeV8(val app: DroidJsApplication) : JSRuntime {
    private val v8: V8=V8.createV8Runtime()
    init {
        v8.registerJavaMethod({ v8obj, v8arr->
            val title=v8arr.getString(0)
            Toast.makeText(app,title,Toast.LENGTH_SHORT).show()

        },"alert")
        val console=Console()
        val _app=App(app)
        v8.add("console",V8Object(v8).apply {
            IConsole::class.java.methods.forEach {method->
                registerJavaMethod(console,method.name,method.name,method.parameterTypes)
            }
        })
        v8.add("app",V8Object(v8).apply {
            IApp::class.java.methods.forEach {method->
                registerJavaMethod(_app,method.name,method.name,method.parameterTypes)
            }
        })
    }
    override fun exec(script: String):Any {
        return v8.executeScript(script)
    }

    override fun close() {

    }
}