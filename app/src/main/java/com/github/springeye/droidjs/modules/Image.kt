package com.github.springeye.droidjs.modules

import androidx.annotation.Keep
import com.github.springeye.droidjs.modules.image.ImageWrapper
import com.github.springeye.droidjs.modules.image.TemplateMatching
import org.opencv.core.Point
import javax.inject.Inject

@Keep
interface IImage{
    fun read(filepath:String,): ImageWrapper?
    fun find(large: ImageWrapper, small: ImageWrapper,weakThreshold:Float=0.7F,threshold:Float=0.9F,maxLevel:Int=TemplateMatching.MAX_LEVEL_AUTO):Point?
}
class Image @Inject constructor(): IImage {
    override fun read(filepath: String): ImageWrapper? {
        TODO("Not yet implemented")
    }

    override fun find(large: ImageWrapper, small: ImageWrapper,weakThreshold:Float,threshold:Float,maxLevel:Int): Point? {

        val mat1 = large.mat
        val mat2 = small.mat
        assert(mat1!=null)
        assert(mat2!=null)
        return TemplateMatching.fastTemplateMatching(mat1!!,
            mat2!!, TemplateMatching.MATCHING_METHOD_DEFAULT,weakThreshold, threshold, maxLevel)
    }
}
