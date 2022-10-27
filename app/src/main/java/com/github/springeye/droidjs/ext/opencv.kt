package com.github.springeye.droidjs.ext

import android.graphics.Bitmap
import android.util.Log
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.ScreenMetrics
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.core.Core.MinMaxLocResult
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.File


fun Bitmap.match(template:Bitmap): Bitmap? {

    return doMatch(this.copy(this.config,true),template.copy(template.config,true))
}
private fun doMatch(screenhot:Bitmap, small:Bitmap): Bitmap? {
    val  THRESHOLD = 0.95;
    val TAG="opencv"
    val _screenhot = Mat()//待匹配的图
    val _small = Mat()//模板图
    Utils.bitmapToMat(small,_small)
    Utils.bitmapToMat(screenhot,_screenhot)
    val templatW=_small.cols();
    val templatH=_small.rows();


    val result_cols: Int = _screenhot.cols() - _small.cols() + 1
    val result_rows: Int = _screenhot.rows() - _small.rows() + 1
    val result = Mat(result_rows,result_cols, CvType.CV_32FC1)
//    Imgproc.matchTemplate(_screenhot,_small, result, Imgproc.TM_CCORR_NORMED)
    Imgproc.matchTemplate(_screenhot,_small, result, Imgproc.TM_CCOEFF_NORMED)
    Core.normalize(result, result, 0.0, 1.0, Core.NORM_MINMAX, -1, Mat())
    val mmr = Core.minMaxLoc(result);
    val matchLoc=mmr.maxLoc
    Log.i("opencv", "Match percentage : " + mmr.maxVal);
    //在原图上的对应模板可能位置画一个绿色矩形
    Imgproc.rectangle(
        _screenhot,
        matchLoc,
        Point(matchLoc.x + templatW, matchLoc.y + templatH),
        Scalar(0.0, 255.0, 0.0),
        8
    )
    if (mmr.maxVal >= THRESHOLD) {
        Log.i(TAG, "matchImages: Match percentage is above the THRESHOLD.");
        Log.i(TAG, "matchImages: ###### Found match . ######");
    } else {
        Log.w(TAG, "matchImages: Match percentage is below the THRESHOLD!!!");
        Log.w(TAG, "matchImages: ****** None match . ******");
    }
    val app = DroidJsApplication.app
    if(app !=null){
        Imgcodecs.imwrite(File(app.filesDir,"result.jpeg").toString(),_screenhot)
    }

    val resultBitmap=Bitmap.createBitmap(screenhot.width,screenhot.height,Bitmap.Config.ARGB_8888)
    Utils.matToBitmap(_screenhot,resultBitmap)
    _small.release()
    _screenhot.release()
    return resultBitmap

}

private fun getMaxLoc(
    clone: Mat,
    result: Mat,
    templatW: Int,
    templatH: Int,
    maxLoc: Point
): MinMaxLocResult? {
    val startY: Int
    val startX: Int
    val endY: Int
    val endX: Int

    //计算大矩形的坐标
    startY = maxLoc.y.toInt()
    startX = maxLoc.x.toInt()

    //计算大矩形的的坐标
    endY = maxLoc.y.toInt() + templatH
    endX = maxLoc.x.toInt() + templatW

    //将大矩形内部 赋值为最大值 使得 以后找的最小值 不会位于该区域  避免找到重叠的目标
    val ch = clone.channels() //通道数 (灰度: 1, RGB: 3, etc.)
    for (i in startX until endX) {
        for (j in startY until endY) {
            val data = clone[j, i] //读取像素值，并存储在double数组中
            for (k in 0 until ch) {        //RGB值或灰度值
                data[k] = 255.0 //对每个像素值（灰度值或RGB通道值，取值0~255）进行处理
            }
            clone.put(j, i, *data) //把处理后的像素值写回到Mat
        }
    }
    val resultH = clone.rows() - result.rows() + 1
    val resultW = clone.cols() - result.cols() + 1
    val result2 = Mat(Size(resultH.toDouble(), resultW.toDouble()), CvType.CV_32FC1)
    Imgproc.matchTemplate(clone, result, result2, Imgproc.TM_CCOEFF_NORMED) //是标准相关性系数匹配  值越大越匹配


//        Imgcodecs.imwrite(getExternalFilesDir("") +"/匹配结果.jpeg", clone);
    //查找result中的最大值 及其所在坐标
    return Core.minMaxLoc(result2)
}
