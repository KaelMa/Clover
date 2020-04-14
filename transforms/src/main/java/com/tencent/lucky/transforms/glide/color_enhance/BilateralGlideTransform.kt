package com.tencent.lucky.transforms.glide.color_enhance

import com.tencent.lucky.transforms.filter.color_enhance.GPUImageBilateralFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BilateralGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * Bilateral, use to enhance image to make sure every pixels is weighted average whit surround.
 */
class BilateralGlideTransform(var distanceNormalizationFactor: Float = 8.0f):
    GlideBaseTransform(GPUImageBilateralFilter(distanceNormalizationFactor))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + distanceNormalizationFactor).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is BilateralGlideTransform && o.distanceNormalizationFactor == distanceNormalizationFactor
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((distanceNormalizationFactor + 1.0f) * 10).toInt()
    }
}