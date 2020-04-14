package com.tencent.lucky.transforms.fresco.transform

import android.graphics.PointF
import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.filter.transform.GPUImageGlassSphereFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class GlassSphereFrescoTransform(private var center: PointF = PointF(0.5f, 0.5f),
                                 private var radius: Float = 0.25f,
                                 private var refractiveIndex: Float = 0.71f
): FrescoBaseTransform(GPUImageGlassSphereFilter(center, radius, refractiveIndex))
{
    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.center=${center.hashCode()}.radius=${radius}.refractiveIndex=${refractiveIndex}")
    }
}