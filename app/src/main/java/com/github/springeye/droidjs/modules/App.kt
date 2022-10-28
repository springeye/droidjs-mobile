package com.github.springeye.droidjs.modules

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.annotation.Keep
import com.github.springeye.droidjs.BuildConfig
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.base.modules.IApp
import javax.inject.Inject


class App @Inject constructor(private val application: Application): IApp {
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

    override fun openAppSettings(packageName: String) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.fromParts("package", packageName, null))
        application.startActivity(intent)
    }

    override fun uninstall(packageName: String) {
        val intent = Intent(Intent.ACTION_DELETE,Uri.fromParts("package", packageName, null))
        application.startActivity(intent)
    }

    override fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW,Uri.parse(url))
        application.startActivity(intent)
    }

    override val versionCode: Int
        get() = BuildConfig.VERSION_CODE
    override val versionName: String
        get() = BuildConfig.VERSION_NAME

}