package com.tencent.lucky.transforms.glide.base

import com.tencent.lucky.transforms.filter.base.GPUImageBrightnessFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: brightness value ranges from -1.0 to 1.0, with 0.0 as the normal level
 */
class BrightnessGlideTransform(var brightness: Float = 0f):
    GlideBaseTransform(GPUImageBrightnessFilter(brightness))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + brightness).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is BrightnessGlideTransform && o.brightness == brightness
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((brightness + 1.0f) * 10).toInt()
    }
}