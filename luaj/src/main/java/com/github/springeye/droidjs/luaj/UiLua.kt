package com.github.springeye.droidjs.luaj

import android.app.Application
import android.util.Log
import com.github.springeye.droidjs.modules.IUi
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.jse.CoerceJavaToLua

class UiLua  constructor(private val application: Application, private val ui: IUi): TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val library= tableOf()
        library.set("findByText", FindByTextFunc(ui))
        env?.apply {
            set("ui",library)
            get("package").get("loaded").set("ui", library);
        }
        return library
    }
    class FindByTextFunc( private val ui: IUi): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val text = arg?.checkstring()?.tojstring()
            Log.d("FindByTextFunc","text=>$text")
            if(text==null) return LuaValue.NIL
            val node=ui.findByText(text)
            if(node!=null) {
                return CoerceJavaToLua.coerce(node)
            }
            return LuaValue.NONE
        }
    }

}