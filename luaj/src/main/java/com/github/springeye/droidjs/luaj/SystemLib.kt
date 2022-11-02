package com.github.springeye.droidjs.luaj

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.github.springeye.droidjs.base.GlobalFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

class SystemLib(private val app:Application,private val globalFunctions: GlobalFunctions): org.luaj.vm2.lib.BaseLib() {

    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val call = super.call(modname, env)
        call.set("toast",Toast(app,globalFunctions))
        call.set("backHome",BackHome(app,globalFunctions))
        call.set("alert",Alert(app,globalFunctions))
        call.set("sleep",Sleep(app,globalFunctions))
        call.set("sendKey",SendKey(app,globalFunctions))
        call.set("sendText",SendText(app,globalFunctions))
        return call
    }
    private class SendKey(private val app:Application,private val globalFunctions: GlobalFunctions): OneArgFunction() {
        override fun call(p0: LuaValue?): LuaValue {
            runBlocking(Dispatchers.Unconfined) {
                p0?.checkint()?.let { globalFunctions.sendKey(it) }
            }
            return NONE
        }

    }
    private class SendText(private val app:Application,private val globalFunctions: GlobalFunctions): OneArgFunction() {
        override fun call(p0: LuaValue?): LuaValue {
            runBlocking(Dispatchers.Unconfined) {
                p0?.checkstring()?.toString()?.let { globalFunctions.sendText(it) }
            }
            return NONE
        }

    }
    private class Sleep(private val app:Application,private val globalFunctions: GlobalFunctions): OneArgFunction() {
        override fun call(p0: LuaValue?): LuaValue {
            runBlocking {
                delay(p0?.checklong()!!)
            }
            return NONE
        }

    }
    private class Alert(private val app:Application,private val globalFunctions: GlobalFunctions): TwoArgFunction() {
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
        override fun call(p0: LuaValue?, p1: LuaValue?): LuaValue {
            if(!checkDialogPermission()){
                return NONE
            }
            val message= p0?.checkstring()?.tojstring()?: return NONE
            val title=p1?.toString()
            runBlocking(Dispatchers.Main) {
                globalFunctions.alert(title, message)
            }
            return NONE
        }
    }
    private class BackHome(private val app:Application,private val globalFunctions: GlobalFunctions): ZeroArgFunction() {
        override fun call(): LuaValue {
            runBlocking(Dispatchers.Main) {
                globalFunctions.backHome()
            }
            return NONE
        }

    }
    private class Toast(private val app:Application,private val globalFunctions: GlobalFunctions): OneArgFunction() {
        override fun call(p0: LuaValue?): LuaValue {
            runBlocking(Dispatchers.Main) {
                globalFunctions.toast(p0?.checkstring()?.toString() ?: "")
            }
            return NONE
        }
    }
}