package com.github.springeye.droidjs.v8

import android.widget.Toast
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.JSRuntime
import com.github.springeye.droidjs.jsmodules.*
fun V8.register(namespace:String,obj:Any): V8Object? {
    val v8Object = V8Object(this)
    add(namespace, v8Object)
    obj::class.java.methods.forEach { method ->
        v8Object.registerJavaMethod(obj, method.name, method.name, method.parameterTypes)
    }
    return v8Object


}
fun V8Object.register(obj:Any) {
    obj::class.java.methods.forEach {method->
        registerJavaMethod(obj,method.name,method.name,method.parameterTypes)
    }
}
fun V8.getV8Object(javaObj:Any): V8Object {
    val obj=V8Object(this)
    javaObj::class.java.methods.forEach {method->
        obj.registerJavaMethod(javaObj,method.name,method.name,method.parameterTypes)
    }
    return obj
}
class JSRuntimeV8(val app: DroidJsApplication) : JSRuntime {
    fun setup(v8:V8) {
        v8.registerJavaMethod({ v8obj, v8arr->
            val title=v8arr.getString(0)
            Toast.makeText(app,title,Toast.LENGTH_SHORT).show()

        },"alert")
        val console=Console()
        val _app=App(app)
        val ui=Ui(v8)
        v8.register("console",console)?.apply { release() }
        v8.register("app",_app)?.apply { release() }
        v8.register("ui",ui)?.apply { release() }



    }
    override fun exec(script: String):Any {
        val v8: V8=V8.createV8Runtime()
        setup(v8)
        val result=v8.executeScript(script)
        v8.release(false)
//        v8.close()
        return result
    }

    override fun close() {

    }
}