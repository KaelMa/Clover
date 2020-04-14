package com.tencent.lucky.transforms.base

import android.graphics.Bitmap

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: ImageMgr
 * Author: kaelma
 * Date: 2020/4/14 10:39 AM
 * Description: image render manager
 */
class Clover {

    companion object {
        fun with(): Clover {
            return Clover()
        }
    }

    private var mBitmap: Bitmap? = null
    private var mFilter: BaseFilter? = null

    /**
     * set source bitmap
     * @param bitmap
     */
    fun setImage(bitmap: Bitmap):Clover {
        this.mBitmap = bitmap
        return this
    }

    /**
     * set filter
     * @param filter
     */
    fun setFilter(filter: BaseFilter): Clover {
        this.mFilter = filter
        return this
    }

    /**
     * get filter bitmap
     * @return
     */
    fun getFilterBitmap(): Bitmap? {
        if (mBitmap == null) {
            return null
        }
        if (mFilter == null) {
            return mBitmap
        }

        val imageRenderer = ImageRenderer(mFilter!!).apply {
            setImageBitmap(mBitmap!!)
        }
        val pixelBuffer = PixelBuffer(mBitmap!!.width, mBitmap!!.height)
        pixelBuffer.setRenderer(imageRenderer)

        val result =  pixelBuffer.getBitmap()
        pixelBuffer.destroy()

        return result
    }
}