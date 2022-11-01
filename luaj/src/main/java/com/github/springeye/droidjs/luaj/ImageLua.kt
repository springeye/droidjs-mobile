package com.github.springeye.droidjs.luaj

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.math.MathUtils
import com.github.springeye.droidjs.base.modules.image.TemplateMatching
import com.github.springeye.droidjs.modules.IUi
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.CoerceLuaToJava
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class ImageLua  constructor(private val application: Application, private val ui: IUi): TwoArgFunction() {
    companion object{
        val LOG_TAG="ImageLua_Native"
    }
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val library= tableOf()
        library.set("read", ReadImageFunc(ui))
        library.set("find", FindImageFunc(ui))
        env?.apply {
            set("image",library)
            get("package").get("loaded").set("image", library);
        }
        return library
    }
    class ReadImageFunc( private val ui: IUi): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val path=arg?.checkstring().toString()
            val bitmap=BitmapFactory.decodeFile(path)
            return if(bitmap!=null){
                val result = Mat();
                Utils.bitmapToMat(bitmap,result)
                bitmap.recycle()
                CoerceJavaToLua.coerce(result)
            }else{
                NIL
            }
        }
    }
    class FindImageFunc( private val ui: IUi): OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            val img:Mat = CoerceLuaToJava.coerce(arg?.checkvalue(1),Mat::class.java) as Mat//大图
            val template:Mat= CoerceLuaToJava.coerce(arg?.checkvalue(2),Mat::class.java) as Mat//小图
            val point=TemplateMatching.fastTemplateMatching(img,template,TemplateMatching.MATCHING_METHOD_DEFAULT,0.75F,0.85F,5)

            return if(point!=null){
                CoerceJavaToLua.coerce(point)
            }else{
                NIL
            }
        }
    }
}