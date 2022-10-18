package com.github.springeye.droidjs.v8

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.JSRuntime
import com.github.springeye.droidjs.jsmodules.*


fun V8Object.register(javaObj:Any) {
    javaObj::class.java.methods.forEach {method->
        registerJavaMethod(javaObj,method.name,method.name,method.parameterTypes)
    }
}
fun V8.getV8Object(javaObj:Any): V8Object {
    val obj=V8Object(this)
    obj.register(javaObj)
    return obj
}
class JSRuntimeV8(val app: DroidJsApplication) : JSRuntime {
    private fun checkDialogPermission(): Boolean {
        return if(Settings.canDrawOverlays(app)){
            true;
        }else{
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:" + app.packageName)
            app.startActivity(intent)
            false
        }
    }
    private fun setup(v8:V8): MutableList<V8Object> {
        val v8Objs= mutableListOf<V8Object>()
        v8.registerJavaMethod({ v8obj, v8arr->
            if(!checkDialogPermission())return@registerJavaMethod
            val message=v8arr.getString(0)
            AlertDialog.Builder(app)
                .setTitle("提示")
                .setMessage(message)
                .create().apply {
                        window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                }.show()
            Toast.makeText(app,message,Toast.LENGTH_SHORT).show()

        },"alert")
        v8.registerJavaMethod({ v8obj, v8arr->
            val message=v8arr.getString(0)
            Toast.makeText(app,message,Toast.LENGTH_SHORT).show()

        },"toast")

        val console=V8Object(v8).apply { v8Objs.add(this) }
        v8.add("console",console)
        console.register(Console())

        val ui=V8Object(v8).apply { v8Objs.add(this) }
        v8.add("ui",ui)
        ui.register(Ui(app,v8))

        val app=V8Object(v8).apply { v8Objs.add(this) }
        v8.add("app",app)

        app.register(App(this.app))



    return v8Objs
    }
    override fun exec(script: String):Any {
        val v8: V8=V8.createV8Runtime()
        val modules=setup(v8)
        val result = try {
            v8.executeScript(script)
        } catch (e: Exception) {
            throw e
        } finally {
            for (module in modules) {
                module.close()
            }
            v8.release(true)
        }

        return result
    }

    override fun close() {

    }
}