package com.github.springeye.droidjs.js

import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.github.springeye.droidjs.modules.IImage
import com.github.springeye.droidjs.modules.image.ImageWrapper
import com.github.springeye.droidjs.modules.image.TemplateMatching
import toV8Object

class ImageJs(private val image: IImage,private val v8:V8):JsModule {
     fun read(filepath: String): ImageWrapper? {
         println("读取图片:${filepath}")

         return null
//         v8.runtime.
    }

     fun find(
         large: ImageWrapper, small: ImageWrapper,weakThreshold:Float=0.7F,threshold:Float=0.9F,maxLevel:Int= TemplateMatching.MAX_LEVEL_AUTO
    ): V8Object? {
        return image.find(large,small,weakThreshold,threshold,maxLevel)?.toV8Object(v8)
    }
}