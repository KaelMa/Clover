package com.tencent.lucky.transforms.glide.adjacent_sample

import com.tencent.lucky.transforms.filter.adjacent_sample.GPUImageCartoonFilter
import com.tencent.lucky.transforms.filter.base.GPUImageHighlightShadowFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: CartoonGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class CartoonGlideTransform(var threshold: Float = 0.2f,
                            var quantizationLevels: Float = 10.0f):
    GlideBaseTransform(GPUImageCartoonFilter(threshold, quantizationLevels))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + threshold + quantizationLevels).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is CartoonGlideTransform && o.threshold == threshold && o.quantizationLevels == quantizationLevels
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((threshold + 1.0f) * 10).toInt() + ((quantizationLevels + 1.0f) * 10).toInt()
    }
}