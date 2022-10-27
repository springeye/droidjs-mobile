package com.github.springeye.droidjs.runtime

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.LuaRuntime
import com.github.springeye.droidjs.modules.App
import com.github.springeye.droidjs.lua.AppLua
import com.github.springeye.droidjs.lua.UiLua
import com.github.springeye.droidjs.modules.IApp
import com.github.springeye.droidjs.modules.IConsole
import com.github.springeye.droidjs.modules.IUi
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaValue
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.jse.JsePlatform
import javax.inject.Inject
class LuaRuntimeLuaj @Inject constructor(val application: DroidJsApplication,
                                         val ui: IUi,
                                         val app: IApp,
                                         val console: IConsole,
):LuaRuntime {
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
    private fun setup(): Globals {
        val globals = JsePlatform.standardGlobals()
        return globals.apply {

            set("alert",object:OneArgFunction(){
                override fun call(p0: LuaValue?): LuaValue {
                    if(!checkDialogPermission())return NONE
                    val message= p0?.checkstring()?.tojstring() ?: return NONE

                    AlertDialog.Builder(application)
                        .setTitle("提示")
                        .setMessage(message)
                        .create().apply {
                            window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                        }.show()
                    return NONE
                }

            })
            set("toast",object:OneArgFunction(){
                override fun call(p0: LuaValue?): LuaValue {
                    val message = p0?.checkstring()?.tojstring()?:return NONE
                    Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
                    return NONE
                }
            })
            load(AppLua(application, app))
            load(UiLua(application, ui))
            LoadState.install(this)
            LuaC.install(this)
        }
    }
    override suspend fun exec(script: String): Any {
        val lua = setup()
        return lua.load(script).call()
    }

    override suspend fun close() {
        TODO("Not yet implemented")
    }
}