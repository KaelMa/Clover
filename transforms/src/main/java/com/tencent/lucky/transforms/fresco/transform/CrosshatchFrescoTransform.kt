package com.tencent.lucky.transforms.fresco.transform

import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.tencent.lucky.transforms.filter.transform.GPUImageCrosshatchFilter
import com.tencent.lucky.transforms.fresco.FrescoBaseTransform

/**
 * Copyright (C), 2019-2020, 腾讯科技（上海）有限公司
 * FileName: BrightnessGlideTransform
 * Author: kaelma
 * Date: 2020/4/14 8:05 PM
 * Description:
 */
class CrosshatchFrescoTransform(private var crossHatchSpacing: Float = 0.03f,
                                private var lineWidth: Float = 0.003f
): FrescoBaseTransform(GPUImageCrosshatchFilter(crossHatchSpacing, lineWidth))
{
    override fun getPostprocessorCacheKey(): CacheKey? {
        return SimpleCacheKey("${name}.crossHatchSpacing=${crossHatchSpacing}.lineWidth=${lineWidth}")
    }
}