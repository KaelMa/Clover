package com.tencent.lucky.transforms.glide.transform

import android.graphics.PointF
import com.tencent.lucky.transforms.filter.transform.GPUImageVignetteFilter
import com.tencent.lucky.transforms.glide.GlideBaseTransform
import java.security.MessageDigest

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: VignetteGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description: 暗角效果
 */
class VignetteGlideTransform(private var center: PointF = PointF(0.5f,0.5f),
                             private var color: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f),
                             private var start: Float = 0.3f,
                             private var end: Float = 0.75f)
: GlideBaseTransform(GPUImageVignetteFilter(center, color, start, end))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + center + color + start + end).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is VignetteGlideTransform && o.center == center && o.color == color && o.start == start && o.end == end
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((start + 1.0f) * 10).toInt() + ((end + 1.0f) * 10).toInt() + center.hashCode() + color.hashCode()
    }
}