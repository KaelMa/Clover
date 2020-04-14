package com.tencent.lucky.transforms.glide.base

import com.tencent.lucky.transforms.filter.base.GPUImageHighlightShadowFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: HighlightShadowGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 * Adjusts the shadows and highlights of an image
 * shadows: Increase to lighten shadows, from 0.0 to 1.0, with 0.0 as the default.
 * highlights: Decrease to darken highlights, from 0.0 to 1.0, with 1.0 as the default.
 */
class HighlightShadowGlideTransform(private var shadows: Float = 0.0f,
                                    private var highlights: Float = 1.0f):
    GlideBaseTransform(GPUImageHighlightShadowFilter(shadows, highlights))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + shadows + highlights).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is HighlightShadowGlideTransform && o.shadows == shadows && o.highlights == highlights
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((shadows + 1.0f) * 10).toInt() + ((highlights + 1.0f) * 10).toInt()
    }
}