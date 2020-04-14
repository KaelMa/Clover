package com.tencent.lucky.transforms.util

import android.content.Context
import android.graphics.*
import android.util.Log
import java.io.IOException
import java.io.InputStream
import kotlin.math.max


/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: kotlinExt
 * Author: kaelma
 * Date: 2020/4/15 2:34 PM
 * Description: bitmap extension function and util function
 */

/**
 * flip horizontal
 */
fun Bitmap.flipHorizontal(): Bitmap {
    val cx = width / 2f
    val cy = height / 2f
    val matrix = Matrix().apply { postScale(-1f, 1f, cx, cy) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

/**
 * flip vertical
 */
fun Bitmap.flipVertical(): Bitmap {
    val cx = width / 2f
    val cy = height / 2f
    val matrix = Matrix().apply { postScale(1f, -1f, cx, cy) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

/**
 * scale
 */
fun Bitmap.scaleBitmap(destWidth: Float, destHeight: Float): Bitmap {
    val scaleW = destWidth / width
    val scaleH = destHeight / height
    val scale = max(scaleW, scaleH)

    val matrix = Matrix().apply {
        postScale(scale,scale)
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

/**
 * center crop scale
 */
fun Bitmap.scaleCenterCrop(newHeight: Int, newWidth: Int): Bitmap {
    val sourceWidth = this.width
    val sourceHeight = this.height

    val xScale = newWidth.toFloat() / sourceWidth
    val yScale = newHeight.toFloat() / sourceHeight
    val scale = Math.max(xScale, yScale)

    val scaledWidth = scale * sourceWidth
    val scaledHeight = scale * sourceHeight

    val left = (newWidth - scaledWidth) / 2
    val top = (newHeight - scaledHeight) / 2

    val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

    val dest = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(dest)
    canvas.drawBitmap(this, null, targetRect, null)

    return dest
}

/**
 * decode bitmap from asset
 * @param context
 * @param path
 */
fun bitmapFromAsset(context: Context, path: String): Bitmap?{
    val assetManager = context.assets
    var istr: InputStream? = null
    var bitmap: Bitmap? = null
    try {
        istr = assetManager.open(path)
        bitmap = BitmapFactory.decodeStream(istr)
    } catch (e: IOException) {
        Log.e("bitmap", e.toString())
    } finally {
        istr?.close()
    }
    return bitmap
}

/**
 * decode bitmap from path, which can be file and assets.
 * assets image must start with "assets://"
 */
const val ASSET_PREFIX = "assets://"
fun bitmapFromPath(context: Context, path: String): Bitmap? {
    return if (path.startsWith(ASSET_PREFIX)) {
        bitmapFromAsset(context, path.substringAfter(ASSET_PREFIX))
    } else {
        BitmapFactory.decodeFile(path)
    }
}