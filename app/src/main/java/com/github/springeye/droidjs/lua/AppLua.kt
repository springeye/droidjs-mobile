package com.github.springeye.droidjs.lua

import android.util.Log
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.modules.App
import com.github.springeye.droidjs.modules.IApp
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import javax.inject.Inject

class AppLua @Inject constructor(private val application: DroidJsApplication, private val app: IApp): TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val library= tableOf()
        library.set("getApkInfo", GetApkInfoFunc(app))
        library.set("getAppName", GetAppNameFunc(app))
        library.set("getPackageName", GetPackageNameFunc(app))
        library.set("launch", LaunchFunc(app))
        library.set("launchApp", LaunchAppFunc(app))
        env?.apply {
            set("app",library)
            get("package").get("loaded").set("app", library);
        }
        return library
    }
    class GetApkInfoFunc( private val app: IApp): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val file = arg?.checkstring()?.tojstring()
            Log.d("GetApkInfoFunc","file=>$file")
            if(file==null) return LuaValue.NIL
            app.getApkInfo(file)
            return LuaValue.NONE
        }
    }
    class GetAppNameFunc( private val app: IApp): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val packageName = arg?.checkstring()?.tojstring()
            Log.d("GetAppNameFunc","packageName=>$packageName")
            if(packageName==null) return LuaValue.NIL
            app.getAppName(packageName)
            return LuaValue.NONE
        }
    }
    class GetPackageNameFunc( private val app: IApp): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val targetAppName = arg?.checkstring()?.tojstring()
            Log.d("getPackageNameFunc","targetAppName=>$targetAppName")
            if(targetAppName==null) return LuaValue.NIL
            app.getPackageName(targetAppName)
            return LuaValue.NONE
        }
    }
    class LaunchAppFunc( private val app: IApp): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val targetAppName = arg?.checkstring()?.tojstring()
            Log.d("LaunchAppFunc","targetAppName=>$targetAppName")
            if(targetAppName==null) return LuaValue.NIL
            app.launchApp(targetAppName)
            return LuaValue.NONE
        }
    }
    class LaunchFunc( private val app: IApp): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val packageName = arg?.checkstring()?.tojstring()
            Log.d("LaunchFunc","packageName=>$packageName")
            if(packageName==null) return LuaValue.NIL
            app.launch(packageName)
            return LuaValue.NONE
        }
    }
}