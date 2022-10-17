package com.github.springeye.droidjs.jsmodules

import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.github.springeye.droidjs.DroidJsApplication
import javax.inject.Inject


interface IApp {
    fun getApkInfo(file:String)
    fun getAppName(packageName:String):String?
    fun getPackageName(targetAppName:String):String?
    fun launch(packageName:String):Boolean
    fun launchApp(targetAppName:String):Boolean
}
class App @Inject constructor(private val application: DroidJsApplication): IApp {
    override fun getApkInfo(file: String) {
        TODO("Not yet implemented")
    }

    override fun getAppName(packageName: String):String? {
        val packageManager: PackageManager = application.packageManager
        for (packageInfo in packageManager.getInstalledPackages(0)) {

            if(packageInfo.packageName==packageName){
                return packageManager.getApplicationLabel(packageInfo.applicationInfo).toString()
            }
        }
        return null
    }

    override fun getPackageName(targetAppName: String):String? {
        val packageManager: PackageManager = application.packageManager
        for (packageInfo in packageManager.getInstalledPackages(0)) {
            val label=packageManager.getApplicationLabel(packageInfo.applicationInfo)
            if(label==targetAppName){
                return packageInfo.packageName
            }
        }
        return null
    }

    override fun launch(packageName: String):Boolean {
        val packageManager: PackageManager = application.packageManager
        var intent: Intent? = Intent()
        intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent == null) {
            Log.w("App","APP not found!")
            return false;
        }
        application.startActivity(intent)
        return true;
    }

    override fun launchApp(targetAppName: String) :Boolean{
        val packageManager: PackageManager = application.packageManager
        for (packageInfo in packageManager.getInstalledPackages(0)) {
            val label=packageManager.getApplicationLabel(packageInfo.applicationInfo)
            if(label==targetAppName){
                application.startActivity(packageManager.getLaunchIntentForPackage(packageInfo.packageName))
                return true
            }
        }
        Log.w("App","APP not found!")
        return false
    }

}