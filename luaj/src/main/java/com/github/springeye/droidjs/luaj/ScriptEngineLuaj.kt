package com.github.springeye.droidjs.luaj

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.github.springeye.droidjs.base.GlobalFunctions
import com.github.springeye.droidjs.base.ScriptRuntime
import com.github.springeye.droidjs.base.modules.IApp
import com.github.springeye.droidjs.base.modules.IConsole
import com.github.springeye.droidjs.modules.IUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaValue
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ZeroArgFunction
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform
import java.io.File
import kotlin.coroutines.CoroutineContext

open class ScriptEngineLuaj  constructor(val application: Application,
                                         val ui: IUi,
                                         val app: IApp,
                                         val console: IConsole,
                                         val globalFunctions: GlobalFunctions
): ScriptRuntime,CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job()
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
                    p0?.checkstring()
                    val message= p0?.checkstring()?.tojstring()?: return NONE
                    val title= p0.checkstring(2)?.tojstring()

//                    AlertDialog.Builder(application)
//                        .setTitle(title)
//                        .setMessage(message)
//                        .create().apply {
//                            window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
//                        }.show()
                    globalFunctions.alert(title,message)
                    return NONE
                }

            })
            set("toast",object:OneArgFunction(){
                override fun call(p0: LuaValue?): LuaValue {
                    val message = p0?.checkstring()?.tojstring()?:return NONE
                    globalFunctions.toast(message);
                    return NONE
                }
            })
            set("backHome",object:ZeroArgFunction(){
                override fun call(): LuaValue {
                    globalFunctions.backHome();
                    return NONE
                }
            })
            set("system",CoerceJavaToLua.coerce(SystemFunctions()))
            load(AppLua(application, app))
            load(UiLua(application, ui))
            load(ImageLua(application, ui))
            LoadState.install(this)
            LuaC.install(this)
        }
    }


    override suspend fun exec(script: String, type: ScriptRuntime.Type): Any? {
        val lua = setup()
        return lua.load(script).call()
    }

    override suspend fun run(file: File, type: ScriptRuntime.Type) {
        TODO("Not yet implemented")
    }

    override suspend fun close() {
        coroutineContext.cancel()
    }
}