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
import org.luaj.vm2.compiler.LuaC
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
        val globals = JsePlatform.debugGlobals()
        return globals.apply {
            load(SystemLib(application,globalFunctions))
            load(AppLua(application, app))
            load(UiLua(application, ui))
            load(ImageLua(application, ui))
            load(DeviceLib(application))
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