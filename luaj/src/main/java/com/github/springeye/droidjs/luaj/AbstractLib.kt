package com.github.springeye.droidjs.luaj

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.CoerceJavaToLua

@Target(AnnotationTarget.FUNCTION
)
@MustBeDocumented
annotation class Export
abstract class AbstractLib: TwoArgFunction() {
    private fun register(library:LuaTable, env: LuaValue?) {
        this::class.java.methods.filter { it.annotations.find { it is Export }!=null }.forEach {method->
            val name=method.name

            val params=method.parameterTypes
            val luaFunc=object:VarArgFunction(){
                override fun onInvoke(args: Varargs?): Varargs {
                    if(args==null) {
                        return CoerceJavaToLua.coerce(method.invoke(this@AbstractLib))
                    }else{
                        val inputs = mutableListOf<Any>()

                        for (i in (params.indices)) {
                            val clazz = params[i]
                            val luaIndex=i+1
                            when (clazz) {
                                String::class.java -> {
                                    inputs.add(args.checkstring(luaIndex).toString())
                                }
                                Double::class.java -> {
                                    inputs.add(args.checkdouble(luaIndex))
                                }
                                Float::class.java -> {
                                    inputs.add(args.checkdouble(luaIndex).toFloat())
                                }
                                Long::class.java -> {
                                    inputs.add(args.checklong(luaIndex))
                                }
                                Int::class.java -> {
                                    inputs.add(args.checkint(luaIndex))
                                }
                                Boolean::class.java -> {
                                    inputs.add(args.checkboolean(luaIndex))
                                }
                                Char::class.java -> {
                                    inputs.add(args.checkint(luaIndex).toChar())
                                }
                                Byte::class.java -> {
                                    inputs.add(args.checkint(luaIndex).toByte())
                                }
                                Short::class.java -> {
                                    inputs.add(args.checkint(luaIndex).toShort())
                                }
                            }


                        }
                        return CoerceJavaToLua.coerce(method.invoke(this@AbstractLib,*inputs.toTypedArray()))
                    }
                }
            }
            library.set(name,luaFunc)
        }
        env?.apply {
            set("ui2",library)
            get("package").get("loaded").set("ui2", library);
        }
    }
    abstract val libName:String
    abstract val functions:Map<String, LibFunction>
    abstract val fields:Map<String, LuaValue>
    final override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val library = LuaTable()
        functions.forEach { (name, func) ->
            library.set(name,func)
        }
        fields.forEach { (name, func) ->
            library.set(name,func)
        }
        register(library,env)
        env?.set(libName, library)
        env?.get("package")?.get("loaded")?.set(libName, library)
        return library;
    }
}