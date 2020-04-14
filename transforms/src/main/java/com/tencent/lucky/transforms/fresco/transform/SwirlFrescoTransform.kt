package com.tencent.lucky.transforms.fresco.transform

import android.graphics.PointF
import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.filter.transform.GPUImageSwirlFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class SwirlFrescoTransform(private var radius: Float = 0.5f,
                           private var angle: Float = 1.0f,
                           private var center: PointF = PointF(0.5f, 0.5f)
): FrescoBaseTransform(GPUImageSwirlFilter(radius, angle, center))
{
    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.radius=${radius}.angle=${angle}.center=\${center.hashCode()")
    }
}