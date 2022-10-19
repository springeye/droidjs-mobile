package com.github.springeye.droidjs.luamodules

import android.util.Log
import com.github.springeye.droidjs.DroidJsApplication
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction

class App(private val application: DroidJsApplication): TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val library= tableOf()
        library.set("launch",LaunchFunc(application))
        env?.apply {
            set("app",library)
            get("package").get("loaded").set("app", library);
        }
        return library
    }


    class LaunchFunc(private val application: DroidJsApplication): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val packageName = arg?.checkstring()?.tojstring()
            Log.d("LaunchFunc","packageName=>$packageName")
            if(packageName==null) return LuaValue.NIL
            com.github.springeye.droidjs.jsmodules.App(application).launch(packageName)
            return LuaValue.NONE
        }
    }
}