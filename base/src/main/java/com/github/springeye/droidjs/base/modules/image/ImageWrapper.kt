package com.github.springeye.droidjs.base.modules.image

import android.graphics.Bitmap
import android.graphics.Color
import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.UncheckedIOException

/**
 * Created by Stardust on 2017/11/25.
 */
class ImageWrapper {
    private var mMat: Mat? = null
    private var mWidth: Int
    private var mHeight: Int
    private var mBitmap: Bitmap? = null

    protected constructor(mat: Mat) {
        mMat = mat
        mWidth = mat.cols()
        mHeight = mat.rows()
    }

    protected constructor(bitmap: Bitmap) {
        mBitmap = bitmap
        mWidth = bitmap.width
        mHeight = bitmap.height
    }

    protected constructor(bitmap: Bitmap, mat: Mat?) {
        mBitmap = bitmap
        mMat = mat
        mWidth = bitmap.width
        mHeight = bitmap.height
    }

    constructor(width: Int, height: Int) : this(
        Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
    ) {
    }

    val width: Int
        get() {
            ensureNotRecycled()
            return mWidth
        }
    val height: Int
        get() {
            ensureNotRecycled()
            return mHeight
        }
    val mat: Mat?
        get() {
            ensureNotRecycled()
            if (mMat == null && mBitmap != null) {
                mMat = Mat()
                Utils.bitmapToMat(mBitmap, mMat)
            }
            return mMat
        }

    fun saveTo(path: String?) {
        ensureNotRecycled()
        if (mBitmap != null) {
            try {
                mBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(path))
            } catch (e: FileNotFoundException) {
                throw UncheckedIOException(e)
            }
        } else {
            Imgcodecs.imwrite(path, mMat)
        }
    }

    fun pixel(x: Int, y: Int): Int {
        ensureNotRecycled()
        if (mBitmap != null) {
            return mBitmap!!.getPixel(x, y)
        }
        val channels = mMat!![x, y]
        return Color.argb(
            channels[3].toInt(),
            channels[0].toInt(),
            channels[1].toInt(),
            channels[2].toInt()
        )
    }

    val bitmap: Bitmap?
        get() {
            ensureNotRecycled()
            if (mBitmap == null && mMat != null) {
                mBitmap =
                    Bitmap.createBitmap(mMat!!.width(), mMat!!.height(), Bitmap.Config.ARGB_8888)
                Utils.matToBitmap(mMat, mBitmap)
            }
            return mBitmap
        }

    fun recycle() {
        if (mBitmap != null) {
            mBitmap!!.recycle()
            mBitmap = null
        }
        if (mMat != null) {
            mMat!!.release()
            mMat = null
        }
    }

    fun ensureNotRecycled() {
        check(!(mBitmap == null && mMat == null)) { "image has been recycled" }
    }

    fun clone(): ImageWrapper? {
        ensureNotRecycled()
        if (mBitmap == null) {
            return ofMat(mMat!!.clone())
        }
        return if (mMat == null) {
            ofBitmap(mBitmap!!.copy(mBitmap!!.config, true))
        } else ImageWrapper(mBitmap!!.copy(mBitmap!!.config, true), mMat!!.clone())
    }

    companion object {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        fun ofImage(image: Image?): ImageWrapper? {
            return if (image == null) {
                null
            } else ImageWrapper(toBitmap(image))
        }

        fun ofMat(mat: Mat?): ImageWrapper? {
            return mat?.let { ImageWrapper(it) }
        }

        fun ofBitmap(bitmap: Bitmap?): ImageWrapper? {
            return bitmap?.let { ImageWrapper(it) }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        fun toBitmap(image: Image): Bitmap {
            val plane = image.planes[0]
            val buffer = plane.buffer
            buffer.position(0)
            val pixelStride = plane.pixelStride
            val rowPadding = plane.rowStride - pixelStride * image.width
            val bitmap = Bitmap.createBitmap(
                image.width + rowPadding / pixelStride,
                image.height,
                Bitmap.Config.ARGB_8888
            )
            bitmap.copyPixelsFromBuffer(buffer)
            return if (rowPadding == 0) {
                bitmap
            } else Bitmap.createBitmap(bitmap, 0, 0, image.width, image.height)
        }
    }
}