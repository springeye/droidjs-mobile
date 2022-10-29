package com.github.springeye.droidjs.luaj

import android.app.Application
import android.util.Log
import com.github.springeye.droidjs.base.modules.IApp
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction

class AppLua  constructor(private val application: Application, private val app: IApp): TwoArgFunction() {
    companion object{
        val LOG_TAG="AppLua_Native"
    }
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
            Log.d(LOG_TAG,"GetApkInfoFunc "+" file=>$file")
            if(file==null) return LuaValue.NIL
            app.getApkInfo(file)
            return LuaValue.NONE
        }
    }
    class GetAppNameFunc( private val app: IApp): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val packageName = arg?.checkstring()?.tojstring()
            Log.d(LOG_TAG,"GetAppNameFunc "+" packageName=>$packageName")
            if(packageName==null) return LuaValue.NIL
            val appName=app.getAppName(packageName)
            return if(appName==null){
                LuaValue.NIL
            }else{
                LuaValue.valueOf(appName)
            }
        }
    }
    class GetPackageNameFunc( private val app: IApp): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val targetAppName = arg?.checkstring()?.tojstring()
            Log.d(LOG_TAG,"getPackageNameFunc "+" targetAppName=>$targetAppName")
            if(targetAppName==null) return LuaValue.NIL
            val packageName=app.getPackageName(targetAppName)
            return if(packageName==null){
                LuaValue.NIL
            }else{
                LuaValue.valueOf(packageName)
            }
        }
    }
    class LaunchAppFunc( private val app: IApp): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val targetAppName = arg?.checkstring()?.tojstring()
            Log.d(LOG_TAG,"LaunchAppFunc "+" targetAppName=>$targetAppName")
            if(targetAppName==null) return LuaValue.NIL
            app.launchApp(targetAppName)
            return LuaValue.NONE
        }
    }
    class LaunchFunc( private val app: IApp): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val packageName = arg?.checkstring()?.tojstring()
            Log.d(LOG_TAG,"LaunchFunc "+" packageName=>$packageName")
            if(packageName==null) return LuaValue.NIL
            app.launch(packageName)
            return LuaValue.NONE
        }
    }
}