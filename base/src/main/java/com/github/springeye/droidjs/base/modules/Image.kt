package com.github.springeye.droidjs.base.modules

import androidx.annotation.Keep
import com.github.springeye.droidjs.base.modules.image.ImageWrapper
import com.github.springeye.droidjs.base.modules.image.TemplateMatching
import org.opencv.core.Point

@Keep
interface IImage{
    fun read(filepath:String,): ImageWrapper?
    fun find(large: ImageWrapper, small: ImageWrapper,weakThreshold:Float=0.7F,threshold:Float=0.9F,maxLevel:Int= TemplateMatching.MAX_LEVEL_AUTO):Point?
}