package com.github.springeye.droidjs.luaj

import org.luaj.vm2.LuaValue

val Boolean.lua: LuaValue
    get() = LuaValue.valueOf(this)

val Int.lua: LuaValue
    get() = LuaValue.valueOf(this)

val Double.lua: LuaValue
    get() = LuaValue.valueOf(this)

val String.lua: LuaValue
    get() = LuaValue.valueOf(this)

val ByteArray.lua: LuaValue
    get() = LuaValue.valueOf(this)