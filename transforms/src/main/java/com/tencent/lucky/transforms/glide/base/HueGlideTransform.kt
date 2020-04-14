package com.tencent.lucky.transforms.glide.base

import com.tencent.lucky.transforms.filter.base.GPUImageHueFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: HueGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * control images hue with this filter
 */
class HueGlideTransform(var hue: Float = 90f):
    GlideBaseTransform(GPUImageHueFilter(hue))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + hue).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is HueGlideTransform && o.hue == hue
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((hue + 1.0f) * 10).toInt()
    }
}