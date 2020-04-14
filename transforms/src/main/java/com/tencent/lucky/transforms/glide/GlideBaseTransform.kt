package com.tencent.lucky.transforms.glide

import android.graphics.Bitmap
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.tencent.lucky.transforms.base.BaseFilter
import com.tencent.lucky.transforms.base.Clover
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: GlideGPUTransform
 * Author: kaelma
 * Date: 2020/4/14 5:42 PM
 * Description: glide gpu transform
 */
open class GlideBaseTransform(var filter: BaseFilter) : BitmapTransformation() {

    val version = 1
    val id: String = "${javaClass.name}.${this.version}"
    open val idBytes = id.toByteArray(Key.CHARSET)

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {

        val outBitmap = Clover.with()
            .setImage(toTransform)
            .setFilter(filter)
            .getFilterBitmap()

        return outBitmap?:toTransform
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(idBytes)
    }

    override fun equals(other: Any?): Boolean {
        return other is GlideBaseTransform
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}