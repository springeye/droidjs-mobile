package com.github.springeye.droidjs.utils

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import java.io.*

fun Context.copyFileOrDir(path:String){
    val assetManager: AssetManager = this.assets
    try {
        val assets:Array<String> = assetManager.list(path)?: arrayOf()
        if (assets.isEmpty()) {
            copyFile(path)
        } else {
            val dir = File(filesDir,path)
            if (!dir.exists()) dir.mkdirs()
            for (i in assets.indices) {
                val path1 = path + "/" + assets[i]
                copyFileOrDir(path1)
            }
        }
    } catch (ex: IOException) {
        Log.e("tag", "I/O Exception", ex)
    }
}
private fun Context.copyFile(filename: String) {
    val assetManager: AssetManager = this.getAssets()
    var `in`: InputStream? = null
    var out: OutputStream? = null
    try {
        `in` = assetManager.open(filename)
        val newFileName  = File(filesDir,filename)
        out = FileOutputStream(newFileName)
        val buffer = ByteArray(1024)
        var read: Int
        while (`in`.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
        `in`.close()
        `in` = null
        out.flush()
        out.close()
        out = null
    } catch (e: Exception) {
        e.printStackTrace()
    }
}