package com.github.springeye.droidjs.runtime

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.JSRuntime
import com.github.springeye.droidjs.js.ConsoleJs
import com.github.springeye.droidjs.js.UiJs
import com.github.springeye.droidjs.modules.App
import com.github.springeye.droidjs.modules.IApp
import com.github.springeye.droidjs.modules.IConsole
import com.github.springeye.droidjs.modules.IUi
import register
import javax.inject.Inject


class JSRuntimeV8 @Inject constructor (val application: DroidJsApplication,
                                       val ui:IUi,
                                       val app:IApp,
                                       val console:IConsole,
) : JSRuntime {
    private fun checkDialogPermission(): Boolean {
        return if(Settings.canDrawOverlays(application)){
            true;
        }else{
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:" + application.packageName)
            application.startActivity(intent)
            false
        }
    }
    private fun setup(v8:V8) {
        v8.registerJavaMethod({ v8obj, v8arr->
            if(!checkDialogPermission())return@registerJavaMethod
            val message=v8arr.getString(0)
            AlertDialog.Builder(application)
                .setTitle("提示")
                .setMessage(message)
                .create().apply {
                        window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                }.show()
            Toast.makeText(application,message,Toast.LENGTH_SHORT).show()

        },"alert")
        v8.registerJavaMethod({ v8obj, v8arr->
            val message=v8arr.getString(0)
            Toast.makeText(application,message,Toast.LENGTH_SHORT).show()

        },"toast")

        val consoleV8=V8Object(v8)
        v8.add("console",consoleV8)


        val uiv8=V8Object(v8)
        v8.add("ui",uiv8)


        val appV8=V8Object(v8)
        v8.add("app",appV8)

        consoleV8.register(ConsoleJs(console))
        uiv8.register(UiJs(ui,v8))
        appV8.register(app)



    }
    override fun exec(script: String):Any {
        val v8: V8=V8.createV8Runtime()
        setup(v8)
        val result = try {
            v8.executeScript(script)
        } catch (e: Exception) {
            throw e
        } finally {
            for (key in v8.keys) {
                v8.getObject(key)?.close()
            }
            v8.release(true)
        }

        return result
    }

    override fun close() {

    }
}