package com.tencent.lucky.transforms.glide.base

import com.tencent.lucky.transforms.filter.base.GPUImageSaturationFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: SaturationGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * saturation: The degree of saturation or desaturation to apply to the image (0.0 - 2.0, with 1.0 as the default)
 */
class SaturationGlideTransform(var saturation: Float = 1f):
    GlideBaseTransform(GPUImageSaturationFilter(saturation))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + saturation).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is SaturationGlideTransform && o.saturation == saturation
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((saturation + 1.0f) * 10).toInt()
    }
}