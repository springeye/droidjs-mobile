package com.github.springeye.droidjs.luaj

import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.ZeroArgFunction
import org.luaj.vm2.lib.jse.CoerceJavaToLua
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Export
class BaseLib {
    private fun register(env: LuaValue?) {
        val library= TwoArgFunction.tableOf()
        this::class.java.methods.filter { it.annotations.find { it is Export }!=null }.forEach {method->
            val name=method.name

            val params=method.parameterTypes
            val luaFun = when (params.size) {
                0 -> {
                    object: ZeroArgFunction(){
                        override fun call(): LuaValue {
                            return CoerceJavaToLua.coerce(method.invoke(this@BaseLib))
                        }
                    }
                }
                1 -> {
                    object: OneArgFunction(){
                        override fun call(p0: LuaValue?): LuaValue {
                            return CoerceJavaToLua.coerce(method.invoke(this@BaseLib))
                        }
                    }
                }
                else -> {
                    object: VarArgFunction(){
                    }
                }
            }
            library.set(name,luaFun)
        }
        env?.apply {
            set("ui2",library)
            get("package").get("loaded").set("ui2", library);
        }
    }
    @Export
    fun findByText(text:String){

    }
}