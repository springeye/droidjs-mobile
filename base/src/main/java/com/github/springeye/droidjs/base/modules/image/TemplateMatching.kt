package com.github.springeye.droidjs.base.modules.image

import android.util.TimingLogger
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

/**
 * Created by Stardust on 2017/11/25.
 */
object TemplateMatching {
    private const val LOG_TAG = "TemplateMatching"
    const val MAX_LEVEL_AUTO = -1
    const val MATCHING_METHOD_DEFAULT = Imgproc.TM_CCOEFF_NORMED
    fun fastTemplateMatching(
        img: Mat,
        template: Mat,
        matchMethod: Int,
        weakThreshold: Float,
        strictThreshold: Float,
        maxLevel: Int
    ): Point? {
        val result = fastTemplateMatching(
            img,
            template,
            matchMethod,
            weakThreshold,
            strictThreshold,
            maxLevel,
            1
        )
        return if (result.isEmpty()) {
            null
        } else result[0].point
    }

    /**
     * 采用图像金字塔算法快速找图
     *
     * @param img             图片
     * @param template        模板图片
     * @param matchMethod     匹配算法
     * @param weakThreshold   弱阈值。该值用于在每一轮模板匹配中检验是否继续匹配。如果相似度小于该值，则不再继续匹配。
     * @param strictThreshold 强阈值。该值用于检验最终匹配结果，以及在每一轮匹配中如果相似度大于该值则直接返回匹配结果。
     * @param maxLevel        图像金字塔的层数
     * @return
     */
    fun fastTemplateMatching(
        img: Mat,
        template: Mat,
        matchMethod: Int,
        weakThreshold: Float,
        strictThreshold: Float,
        maxLevel: Int,
        limit: Int
    ): List<Match> {
        var maxLevel = maxLevel
        val logger = TimingLogger(LOG_TAG, "fast_tm")
        if (maxLevel == MAX_LEVEL_AUTO) {
            //自动选取金字塔层数
            maxLevel = selectPyramidLevel(img, template)
            logger.addSplit("selectPyramidLevel:$maxLevel")
        }
        //保存每一轮匹配到模板图片在原图片的位置
        val finalMatchResult: MutableList<Match> = ArrayList()
        var previousMatchResult = emptyList<Match>()
        var isFirstMatching = true
        for (level in maxLevel downTo 0) {
            // 放缩图片
            val currentMatchResult: MutableList<Match> = ArrayList()
            val src = getPyramidDownAtLevel(img, level)
            val currentTemplate = getPyramidDownAtLevel(template, level)
            // 如果在上一轮中没有匹配到图片，则考虑是否退出匹配
            if (previousMatchResult.isEmpty()) {
                // 如果不是第一次匹配，并且不满足shouldContinueMatching的条件，则直接退出匹配
                if (!isFirstMatching && !shouldContinueMatching(level, maxLevel)) {
                    break
                }
                val matchResult = matchTemplate(src, currentTemplate, matchMethod)
                getBestMatched(
                    matchResult,
                    currentTemplate,
                    matchMethod,
                    weakThreshold,
                    currentMatchResult,
                    limit,
                    null
                )
                matchResult.release()
            } else {
                for (match in previousMatchResult) {
                    // 根据上一轮的匹配点，计算本次匹配的区域
                    val r = getROI(match.point, src, currentTemplate)
                    val m = Mat(src, r)
                    val matchResult = matchTemplate(m, currentTemplate, matchMethod)
                    getBestMatched(
                        matchResult,
                        currentTemplate,
                        matchMethod,
                        weakThreshold,
                        currentMatchResult,
                        limit,
                        r
                    )
                    m.release()
                    matchResult.release()
                }
            }
            if (src !== img) src.release()
            if (currentTemplate !== template) currentTemplate.release()
            logger.addSplit("level:$level, result:$previousMatchResult")

            // 把满足强阈值的点找出来，加到最终结果列表
            if (!currentMatchResult.isEmpty()) {
                val iterator = currentMatchResult.iterator()
                while (iterator.hasNext()) {
                    val match = iterator.next()
                    if (match.similarity >= strictThreshold) {
                        pyrUp(match.point, level)
                        finalMatchResult.add(match)
                        iterator.remove()
                    }
                }
                // 如果所有结果都满足强阈值，则退出循环，返回最终结果
                if (currentMatchResult.isEmpty()) {
                    break
                }
            }
            isFirstMatching = false
            previousMatchResult = currentMatchResult
        }
        logger.addSplit("result:$finalMatchResult")
        logger.dumpToLog()
        return finalMatchResult
    }

    private fun getPyramidDownAtLevel(m: Mat, level: Int): Mat {
        if (level == 0) {
            return m
        }
        var cols = m.cols()
        var rows = m.rows()
        for (i in 0 until level) {
            cols = (cols + 1) / 2
            rows = (rows + 1) / 2
        }
        val r = Mat(rows, cols, m.type())
        Imgproc.resize(m, r, Size(cols.toDouble(), rows.toDouble()))
        return r
    }

    private fun pyrUp(p: Point, level: Int) {
        for (i in 0 until level) {
            p.x *= 2.0
            p.y *= 2.0
        }
    }

    private fun shouldContinueMatching(level: Int, maxLevel: Int): Boolean {
        if (level == maxLevel && level != 0) {
            return true
        }
        return if (maxLevel <= 2) {
            false
        } else level == maxLevel - 1
    }

    private fun getROI(p: Point, src: Mat, currentTemplate: Mat): Rect {
        var x = (p.x * 2 - currentTemplate.cols() / 4).toInt()
        x = Math.max(0, x)
        var y = (p.y * 2 - currentTemplate.rows() / 4).toInt()
        y = Math.max(0, y)
        var w = (currentTemplate.cols() * 1.5).toInt()
        var h = (currentTemplate.rows() * 1.5).toInt()
        if (x + w >= src.cols()) {
            w = src.cols() - x - 1
        }
        if (y + h >= src.rows()) {
            h = src.rows() - y - 1
        }
        return Rect(x, y, w, h)
    }

    private fun selectPyramidLevel(img: Mat, template: Mat): Int {
        val minDim = Nath.min(img.rows(), img.cols(), template.rows(), template.cols())
        //这里选取16为图像缩小后的最小宽高，从而用log(2, minDim / 16)得到最多可以经过几次缩小。Nath
        val maxLevel = (Math.log((minDim / 16).toDouble()) / Math.log(2.0)).toInt()
        return if (maxLevel < 0) {
            0
        } else Math.min(6, maxLevel)
        //上限为6
    }

    private fun matchTemplate(img: Mat, temp: Mat, match_method: Int): Mat {
        val result_cols = img.cols() - temp.cols() + 1
        val result_rows = img.rows() - temp.rows() + 1
        val result = Mat(result_rows, result_cols, CvType.CV_32FC1)
        Imgproc.matchTemplate(img, temp, result, match_method)
        return result
    }

    private fun getBestMatched(
        tmResult: Mat,
        template: Mat,
        matchMethod: Int,
        weakThreshold: Float,
        outResult: MutableList<Match>,
        limit: Int,
        rect: Rect?
    ) {
        for (i in 0 until limit) {
            val bestMatched = getBestMatched(tmResult, matchMethod, weakThreshold, rect)
                ?: break
            outResult.add(bestMatched)
            val start = Point(
                Math.max(0.0, bestMatched.point.x - template.width() + 1),
                Math.max(0.0, bestMatched.point.y - template.height() + 1)
            )
            val end = Point(
                Math.min(tmResult.width().toDouble(), bestMatched.point.x + template.width()),
                Math.min(tmResult.height().toDouble(), bestMatched.point.y + template.height())
            )
            Imgproc.rectangle(tmResult, start, end, Scalar(0.0, 255.0, 0.0), -1)
        }
    }

    private fun getBestMatched(
        tmResult: Mat,
        matchMethod: Int,
        weakThreshold: Float,
        rect: Rect?
    ): Match? {
        val logger = TimingLogger(LOG_TAG, "best_matched_point")
        val mmr = Core.minMaxLoc(tmResult)
        logger.addSplit("minMaxLoc")
        val value: Double
        val pos: Point
        if (matchMethod == Imgproc.TM_SQDIFF || matchMethod == Imgproc.TM_SQDIFF_NORMED) {
            pos = mmr.minLoc
            value = -mmr.minVal
        } else {
            pos = mmr.maxLoc
            value = mmr.maxVal
        }
        if (value < weakThreshold) {
            return null
        }
        if (rect != null) {
            pos.x += rect.x.toDouble()
            pos.y += rect.y.toDouble()
        }
        logger.addSplit("value:$value")
        return Match(pos, value)
    }

    class Match(val point: Point, val similarity: Double) {
        override fun toString(): String {
            return "Match{" +
                    "point=" + point +
                    ", similarity=" + similarity +
                    '}'
        }
    }
}