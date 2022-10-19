package com.github.springeye.droidjs.lua

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.LuaRuntime
import com.github.springeye.droidjs.luamodules.App
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaValue
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.jse.JsePlatform

class LuaRuntimeLuaj(val app: DroidJsApplication):LuaRuntime {
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
    private fun setup(): Globals {
        val globals = JsePlatform.standardGlobals()
        return globals.apply {
            load(App(app))
            set("alert",object:OneArgFunction(){
                override fun call(p0: LuaValue?): LuaValue {
                    if(!checkDialogPermission())return NONE
                    val message= p0?.checkstring()?.tojstring() ?: return NONE

                    AlertDialog.Builder(app)
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
                    Toast.makeText(app, message, Toast.LENGTH_SHORT).show()
                    return NONE
                }
            })
            LoadState.install(this)
            LuaC.install(this)
        }
    }
    override fun exec(script: String): Any {
        val lua = setup()
        return lua.load(script).call()
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}