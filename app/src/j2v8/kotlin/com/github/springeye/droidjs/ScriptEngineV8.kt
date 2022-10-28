package com.github.springeye.droidjs

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.ScriptRuntime
import com.github.springeye.droidjs.js.AppJs
import com.github.springeye.droidjs.js.ConsoleJs
import com.github.springeye.droidjs.js.ImageJs
import com.github.springeye.droidjs.js.UiJs
import com.github.springeye.droidjs.modules.IApp
import com.github.springeye.droidjs.modules.IConsole
import com.github.springeye.droidjs.modules.IImage
import com.github.springeye.droidjs.modules.IUi
import registerJavaObject
import java.io.File
import javax.inject.Inject


class ScriptEngineV8 @Inject constructor (val application: DroidJsApplication,
                                          val ui:IUi,
                                          val app:IApp,
                                          val console:IConsole,
                                          val image:IImage,
) : ScriptRuntime {
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
    private fun setup(v8:V8): List<V8Object> {
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





        return listOf<V8Object>(
            v8.registerJavaObject("app",AppJs(app)),
            v8.registerJavaObject("console",ConsoleJs(console)),
            v8.registerJavaObject("ui",UiJs(ui,v8)),
            v8.registerJavaObject("image",ImageJs(image,v8)),
        )



    }

    private fun release(obj: List<V8Object>){
        for (o in obj) {
            o.close()
        }
    }

    override suspend fun exec(script: String, type: ScriptRuntime.Type): Any? {

        val v8: V8=V8.createV8Runtime()
        val registedModule=setup(v8)
        val result = try {
            v8.executeScript(script)
        } catch (e: Exception) {
            throw e
        } finally {
            release(registedModule)
            v8.release(true)
        }

        return result
    }

    override suspend fun run(file: File, type: ScriptRuntime.Type) {
        TODO("Not yet implemented")
    }

    override suspend fun close() {

    }
}