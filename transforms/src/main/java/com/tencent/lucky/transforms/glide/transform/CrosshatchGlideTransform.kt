package com.tencent.lucky.transforms.glide.transform

import com.tencent.lucky.transforms.filter.transform.GPUImageCrosshatchFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: CrosshatchGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: 交叉阴影特效
 */
class CrosshatchGlideTransform(private var crossHatchSpacing: Float = 0.03f,
                               private var lineWidth: Float = 0.003f
): GlideBaseTransform(GPUImageCrosshatchFilter(crossHatchSpacing, lineWidth))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + crossHatchSpacing + lineWidth).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is CrosshatchGlideTransform && o.crossHatchSpacing == crossHatchSpacing && o.lineWidth == lineWidth
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((crossHatchSpacing + 1.0f) * 10).toInt() + ((lineWidth + 1.0f) * 10).toInt()
    }
}