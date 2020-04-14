package com.tencent.lucky.transforms.glide.base

import com.tencent.lucky.transforms.filter.base.GPUImageOpacityFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: OpacityGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * Adjusts the alpha channel of the incoming image
 * opacity: The value to multiply the incoming alpha channel for each pixel by (0.0 - 1.0, with 1.0 as the default)
 */
class OpacityGlideTransform(var opacity: Float = 1.0f):
    GlideBaseTransform(GPUImageOpacityFilter(opacity))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + opacity).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is OpacityGlideTransform && o.opacity == opacity
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((opacity + 1.0f) * 10).toInt()
    }
}