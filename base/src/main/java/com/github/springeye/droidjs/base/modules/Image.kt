package com.github.springeye.droidjs.base.modules

import com.github.springeye.droidjs.base.modules.image.ImageWrapper
import com.github.springeye.droidjs.base.modules.image.TemplateMatching
import org.opencv.core.Point


class Image  constructor(): IImage {
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
