package com.github.springeye.droidjs.luaj

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.ZeroArgFunction
import org.luaj.vm2.lib.jse.CoerceJavaToLua
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Export
abstract class AbstractLib: TwoArgFunction() {
    private fun register(library:LuaTable, env: LuaValue?) {
        this::class.java.methods.filter { it.annotations.find { it is Export }!=null }.forEach {method->
            val name=method.name

            val params=method.parameterTypes
            val luaFun = when (params.size) {
                0 -> {
                    object: ZeroArgFunction(){
                        override fun call(): LuaValue {
                            return CoerceJavaToLua.coerce(method.invoke(this@AbstractLib))
                        }
                    }
                }
                1 -> {
                    object: OneArgFunction(){
                        override fun call(p0: LuaValue?): LuaValue {
                            return CoerceJavaToLua.coerce(method.invoke(this@AbstractLib))
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
    abstract val libName:String
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val tables = LuaTable()
        env?.set(libName, tables)
        env?.get("package")?.get("loaded")?.set(libName, tables)
        return tables;
    }
}