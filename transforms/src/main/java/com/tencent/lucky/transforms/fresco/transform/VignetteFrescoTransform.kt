package com.tencent.lucky.transforms.fresco.transform

import android.graphics.PointF
import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.filter.transform.GPUImageVignetteFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class VignetteFrescoTransform(private var center: PointF = PointF(0.5f,0.5f),
                              private var color: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f),
                              private var start: Float = 0.3f,
                              private var end: Float = 0.75f)
: FrescoBaseTransform(GPUImageVignetteFilter(center, color, start, end))
{
    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.center=${center.hashCode()}.color=${color.hashCode()}.start=${start}.end=${end}")
    }
}