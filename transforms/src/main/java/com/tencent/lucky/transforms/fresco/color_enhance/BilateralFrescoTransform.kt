package com.tencent.lucky.transforms.fresco.color_enhance

import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.filter.color_enhance.GPUImageBilateralFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class BilateralFrescoTransform(var distanceNormalizationFactor: Float = 8.0f):
    FrescoBaseTransform(GPUImageBilateralFilter(distanceNormalizationFactor))
{
    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.distanceNormalizationFactor=$distanceNormalizationFactor")
    }
}