package com.tencent.lucky.transforms.glide.base

import com.tencent.lucky.transforms.filter.base.GPUImageSharpenFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: SharpenGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * Sharpens the picture.
 * sharpness: from -4.0 to 4.0, with 0.0 as the normal level
 */
class SharpenGlideTransform(var sharpness: Float = 1f):
    GlideBaseTransform(GPUImageSharpenFilter(sharpness))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + sharpness).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is SharpenGlideTransform && o.sharpness == sharpness
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((sharpness + 1.0f) * 10).toInt()
    }
}